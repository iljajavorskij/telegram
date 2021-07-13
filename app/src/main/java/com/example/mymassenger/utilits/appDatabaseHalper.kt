package com.example.mymassenger.utilits

import android.net.Uri
import android.os.storage.StorageManager
import android.provider.ContactsContract
import com.example.mymassenger.models.CommonModel
import com.example.mymassenger.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var AUTH:FirebaseAuth//эта переменная инициализируется один раз в мэйн активити и работает во всем приложении
lateinit var REF_DATABASE_ROOT:DatabaseReference
lateinit var UID:String
lateinit var USER:User
lateinit var REF_STORAGE_ROOT:StorageReference

const val NODE_USER = "user"
const val CHILD_ID = "id"
const val NODE_USERNAMES = "usernames"


const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "userName"
const val CHILD_FULLNAME = "fullname"
const val CHILD_BIO = "bio"
const val FOLDER_PROFILE_IMAGE = "profile_image"
const val CHILD_PHOTO = "photoUrl"
const val CHILD_STATE = "state"

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

inline fun putImageToStorge(uri: Uri, path: StorageReference,crossinline function: () -> Unit) {
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
    }

}
