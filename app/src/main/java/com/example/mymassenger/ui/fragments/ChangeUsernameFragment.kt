package com.example.mymassenger.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mymassenger.MainActivity
import com.example.mymassenger.R
import com.example.mymassenger.databinding.FragmentChangeUsernameBinding
import com.example.mymassenger.models.User
import com.example.mymassenger.utilits.*
import java.util.*


class ChangeUsernameFragment : BaseChangeFragment(R.layout.fragment_change_username) {
    lateinit var mBinding: FragmentChangeUsernameBinding
    lateinit var mUsernameContainer:TextView
    lateinit var username:String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentChangeUsernameBinding.inflate(layoutInflater,container,false)
        mUsernameContainer = mBinding.changeUsername

        return mBinding.root
    }



    override fun onResume() {
        super.onResume()
        mUsernameContainer.text = USER.userName
    }



     override fun change() {
        username = mUsernameContainer.text.toString().toLowerCase(Locale.getDefault())
        if (username.isEmpty()){
            showToast("введите имя")
        }else{

            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener{
                    if (it.hasChild(username)){
                        showToast("такой пользователь уже есть")
                    }else{
                        changeUsername()
                        //replaceFragment(SettingsFragment())
                    }
                })



        }
    }

    private fun changeUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(username).setValue(UID)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    updateCurrentUsername()
                }
            }
    }

    private fun updateCurrentUsername() {
        REF_DATABASE_ROOT.child(NODE_USER).child(UID).child(CHILD_USERNAME)
            .setValue(username).addOnCompleteListener {
                if (it.isSuccessful){
                    showToast("Ok")
                    deleteOldUsername()
                }else{
                    showToast(it.exception?.message.toString())
                }
            }
    }

    private fun deleteOldUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(USER.userName).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    showToast("Ok")
                    fragmentManager?.popBackStack()
                    USER.userName = username
                }else{
                    showToast(it.exception?.message.toString())
                }
            }
    }
}

