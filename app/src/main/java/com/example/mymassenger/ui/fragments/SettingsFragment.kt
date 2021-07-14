package com.example.mymassenger.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.mymassenger.MainActivity
import com.example.mymassenger.R
import com.example.mymassenger.databinding.FragmentSettingsBinding
import com.example.mymassenger.utilits.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import de.hdodenhof.circleimageview.CircleImageView


class SettingsFragment : BlankFragment(R.layout.fragment_settings) {

    private lateinit var mSettingsBinding: FragmentSettingsBinding
    private lateinit var mfullname:TextView
    private lateinit var userName:TextView
    private lateinit var bio:TextView
    private lateinit var status:TextView
    private lateinit var phone:TextView
    private lateinit var userNumeBottom:TextView
    private lateinit var btn : ConstraintLayout
    private lateinit var btnBio:ConstraintLayout
    private lateinit var fotobtn: CircleImageView
    private lateinit var photoUser:CircleImageView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mSettingsBinding = FragmentSettingsBinding.inflate(layoutInflater,container,false)
        mfullname = mSettingsBinding.settingsUserName
        bio = mSettingsBinding.settingsUserInfo
        userName = mSettingsBinding.settingsNameUser
        status = mSettingsBinding.settingsStatus
        phone = mSettingsBinding.settingsPhoneNumber
        btn = mSettingsBinding.settingsChangeUsername
        userNumeBottom = mSettingsBinding.settingsUserName
        btnBio = mSettingsBinding.settingsChanfInfo
        fotobtn = mSettingsBinding.settingChangFoto
        photoUser = mSettingsBinding.settingUserFoto
        return mSettingsBinding.root
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
        APP_ACTIVITY.mAppDriwer.updateHeader()


    }

    private fun initFields() {
        mfullname.text =  USER.userName
        bio.text = USER.bio
        userName.text = USER.fullname
        status.text = USER.state
        phone.text = USER.phone
        btn.setOnClickListener { replaceFragment(ChangeUsernameFragment()) }
        btnBio.setOnClickListener { replaceFragment(ChangeBioFragment()) }
        fotobtn.setOnClickListener{changePhotoUser()}
        photoUser.dowloadAndSetImage(USER.photoUrl)



    }

    private fun changePhotoUser() {
        CropImage.activity()
            .setAspectRatio(1,1)
            .setRequestedSize(600,600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY,this)
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.setting_menu_up,menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.setting_up_menu_exit -> {
                AppState.updateState(AppState.OFFLINE)
                AUTH.signOut()
                restartActivity()
            }
            R.id.setting_menu_chanch_name -> {
                replaceFragment(ChangeNameFragment())
            }
        }
        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK && data != null) {
            val uri = CropImage.getActivityResult(data).uri//получаем ури области которая обрезана из активитирезалт
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE).child(UID)//создаю путь

            putImageToStorge(uri,path){
                getUrlFromStirage(path){
                     putUrlToDatabase(it){
                        photoUser.dowloadAndSetImage(it)
                        showToast("фото добавлено в базу данных")
                        USER.photoUrl = it
                         APP_ACTIVITY.mAppDriwer.updateHeader()
                    }
                }
            }

            /*path.putFile(uri).addOnCompleteListener{
                if (it.isSuccessful){
                    path.downloadUrl.addOnCompleteListener {
                        if (it.isSuccessful){
                            val photoUrl = it.result.toString()
                            REF_DATABASE_ROOT
                                .child(NODE_USER)
                                .child(UID)
                                .child(CHILD_PHOTO)
                                .setValue(photoUrl).addOnCompleteListener {
                                    photoUser.dowloadAndSetImage(photoUrl)
                                    showToast("фото добавлено в базу данных")
                                    USER.photoUrl = photoUrl
                                }
                        }
                    }
                }
            }*/
        }
    }


}
