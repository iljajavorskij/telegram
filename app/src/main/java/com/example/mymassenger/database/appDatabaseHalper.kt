package com.example.mymassenger.utilits

import android.net.Uri
import android.provider.ContactsContract
import com.example.mymassenger.models.CommonModel
import com.example.mymassenger.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var AUTH:FirebaseAuth//эта переменная инициализируется один раз в мэйн активити и работает во всем приложении
lateinit var REF_DATABASE_ROOT:DatabaseReference
lateinit var UID:String
lateinit var USER:User
lateinit var REF_STORAGE_ROOT:StorageReference


const val TYPE_TEXT = "text"
const val TYPE_VOICE = "VOICE"

const val NODE_USER = "user"
const val CHILD_ID = "id"
const val NODE_USERNAMES = "usernames"
const val NODE_PHONES = "phones"
const val NODE_PHONES_CONTACT = "phones_contact"
const val NODE_MESSAGES = "messages"
const val FOLDER_MESSAGES_IMAGES = "message_image"
const val FOLDER_FILES = "messages_files"


const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "userName"
const val CHILD_FULLNAME = "fullname"
const val CHILD_BIO = "bio"
const val FOLDER_PROFILE_IMAGE = "profile_image"
const val CHILD_PHOTO = "photoUrl"
const val CHILD_STATE = "state"
const val CHILD_IMAGE_URL = "imageUrl"


const val CHILD_TEXT = "text"
const val CHILD_TYPE = "type"
const val CHILD_FROM = "from"
const val CHILD_TIMESTAPE = "timeStamp"


fun initFirebase(){
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    USER = User()
    UID = AUTH.currentUser?.uid.toString()
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}

inline fun putUrlToDatabase(url: String,crossinline function: () -> Unit) {
    REF_DATABASE_ROOT
        .child(NODE_USER)
        .child(UID)
        .child(CHILD_PHOTO)
        .setValue(url)
        .addOnSuccessListener {function()}
}

inline fun getUrlFromStirage(path: StorageReference,crossinline function: (url:String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener {function(it.toString())}
}

inline fun putFileToStorge(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener {function()}

}

inline fun initUser(crossinline function: () -> Unit ) {
    REF_DATABASE_ROOT.child(NODE_USER).child(UID)
        .addListenerForSingleValueEvent(AppValueEventListener{
            USER = it.getValue(User::class.java) ?: User()
            if (USER.userName.isEmpty()){
                USER.userName = UID
            }
            function()
        })
}

fun initContacts() {
    if (checkPermission(READ_CONTACT)){
        val arrayContacts = arrayListOf<CommonModel>()
        val cursor = APP_ACTIVITY.contentResolver.query(//сщздаю курсор который будет идти по списку контактов
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI//задаю место где будет двигаться курсор
        ,null
        ,null
        ,null
        ,null
        )

        cursor?.let {//запускаеся если курсор не нул
            while (it.moveToNext()){//прохожусь циклом по контактам и записываю в переменные
                val fillname = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))//
                val phone = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val modelNew = CommonModel()
                modelNew.fullname = fillname
                modelNew.phone = phone.replace(Regex("[\\s,-]"),"")
                arrayContacts.add(modelNew)
            }
        }
        cursor?.close()
        updatePhoneToDatabase(arrayContacts)
    }

}

fun updatePhoneToDatabase(arrayContacts: ArrayList<CommonModel>) {
    if (AUTH.currentUser != null){
        REF_DATABASE_ROOT.child(NODE_PHONES).addListenerForSingleValueEvent(AppValueEventListener{
            it.children.forEach{snapshot ->
                arrayContacts.forEach {contact ->
                    if (snapshot.key == contact.phone) {
                        REF_DATABASE_ROOT.child(NODE_PHONES_CONTACT).child(UID).child(snapshot.value.toString()).child(
                            CHILD_ID ).setValue(snapshot.value.toString())
                    }
                }
            }
        })
    }

}

fun DataSnapshot.getCommonModel(): CommonModel
        = this.getValue(CommonModel::class.java)?: CommonModel()



fun DataSnapshot.getUserModel(): User
        = this.getValue(User::class.java)?: User()


 fun sendMessage(message: String, receiving_id: String, typeText: String, function: () -> Unit) {
     val refDialogUser = "$NODE_MESSAGES/$UID/$receiving_id"
     val refDialogReceivingUser = "$NODE_MESSAGES/$receiving_id/$UID"
     val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key

     val mapMessage = hashMapOf<String,Any>()
     mapMessage[CHILD_FROM] = UID
     mapMessage[CHILD_TYPE] = typeText
     mapMessage[CHILD_TEXT] = message
     mapMessage[CHILD_ID] = messageKey.toString()
     mapMessage[CHILD_TIMESTAPE] = ServerValue.TIMESTAMP//устанавливаю время взятое с сервера фаербэйс

     val mapDialog = hashMapOf<String,Any>()
     mapDialog["$refDialogUser/$messageKey"] = mapMessage
     mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

     REF_DATABASE_ROOT
         .updateChildren(mapDialog)
         .addOnSuccessListener { function() }
         .addOnFailureListener{ showToast(it.message.toString())}
}


fun updateCurrentUsername(newUsername:String) {
    REF_DATABASE_ROOT.child(NODE_USER).child(UID).child(CHILD_USERNAME)
        .setValue(newUsername).addOnCompleteListener {
            if (it.isSuccessful){
                showToast("Ok")
                deleteOldUsername(newUsername)
            }else{
                showToast(it.exception?.message.toString())
            }
        }
}

private fun deleteOldUsername(newUsername:String) {
    REF_DATABASE_ROOT.child(NODE_USERNAMES).child(USER.userName).removeValue()
        .addOnSuccessListener {
                showToast("Ok")
                APP_ACTIVITY.supportFragmentManager.popBackStack()
                USER.userName = newUsername
        }.addOnFailureListener{
            showToast(it.message.toString())
        }
}

fun setBioToDatabase(newBio: String) {
    REF_DATABASE_ROOT
        .child(NODE_USER)
        .child(UID)
        .child(CHILD_BIO)
        .setValue(newBio)
        .addOnSuccessListener {
                showToast("ok")
                USER.bio = newBio
                APP_ACTIVITY.supportFragmentManager.popBackStack()
        }.addOnFailureListener{
            showToast(it.message.toString())
        }
}

fun setNameToDatabase(fullName: String) {
    REF_DATABASE_ROOT//лбращаемся к базе по главвной ссыдке telegram-46746-default-rtdb
        .child(NODE_USER)//обращаемся к ноде юзерс следующей по дереву
        .child(UID)//обращаемся к уникальному идентификационному номену сделующему по дереву
        .child(CHILD_FULLNAME)//добавляем чайлд фуллнаме и присваиваем ему значение из переменной
        .setValue(fullName)
        .addOnSuccessListener {//этот слушатель запустится только все пройдет как по маслу
                USER.fullname = fullName
                APP_ACTIVITY.mAppDriwer.updateHeader()
                APP_ACTIVITY.supportFragmentManager.popBackStack()//устанавливаем обработчик события
        }.addOnFailureListener {
            showToast(it.message.toString())
        }
}

fun sendMessageAsFile(receiving_id: String, fileUrl: String, messageKey: String, typeMessage:String) {
    val refDialogUser = "$NODE_MESSAGES/$UID/$receiving_id"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receiving_id/$UID"

    val mapMessage = hashMapOf<String,Any>()
    mapMessage[CHILD_FROM] = UID
    mapMessage[CHILD_TYPE] = typeMessage 
    mapMessage[CHILD_ID] = messageKey
    mapMessage[CHILD_TIMESTAPE] = ServerValue.TIMESTAMP//устанавливаю время взятое с сервера фаербэйс
    mapMessage[CHILD_IMAGE_URL] = fileUrl

    val mapDialog = hashMapOf<String,Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT
        .updateChildren(mapDialog)
        .addOnFailureListener{ showToast(it.message.toString())}
}

fun getMessageKey(id:String) = REF_DATABASE_ROOT
.child(NODE_MESSAGES)
.child(UID)
.child(id)
.push().key.toString()


fun uploadFileToStorage(uri:Uri,messageKey:String,receivedId:String,typeMessage:String) {
    val path = REF_STORAGE_ROOT.child(FOLDER_FILES).child(messageKey)//создаю путь

        putFileToStorge(uri, path) {
        getUrlFromStirage(path) {
            putUrlToDatabase(it) {
                sendMessageAsFile(receivedId ,it,messageKey,typeMessage)
            }
        }
    }}