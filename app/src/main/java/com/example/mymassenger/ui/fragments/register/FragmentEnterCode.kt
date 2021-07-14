package com.example.mymassenger.ui.fragments.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.example.mymassenger.MainActivity
import com.example.mymassenger.R
import com.example.mymassenger.databinding.FragmentEnterCodeBinding
import com.example.mymassenger.ui.fragments.BlankFragment
import com.example.mymassenger.utilits.*
import com.google.firebase.auth.PhoneAuthProvider


class FragmentEnterCode(val mPhoneNumber: String,val id: String) : BlankFragment(R.layout.fragment_enter_code) {

    private lateinit var mBinding: FragmentEnterCodeBinding
    private lateinit var mEditText: EditText
    private lateinit var mTextView: TextView




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentEnterCodeBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart() 
        APP_ACTIVITY.title = mPhoneNumber
            mEditText = mBinding.registerCode
        mTextView = mBinding.textCode

        mEditText.addTextChangedListener(AppTextWatcher{

                val string = mEditText.text.toString().trim()
                if (string.length == 6){
                    mTextView.text = "Спасибо"
                    enterCode()
            }
        })

    }

    private fun enterCode() {
        val code = mEditText.text.toString()
        val credential = PhoneAuthProvider.getCredential(id,code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->//если пользователь найден то запускаю мэйн активити вместо регист
            if (task.isSuccessful){

                val uid = AUTH.currentUser?.uid.toString()//беру айди пользователья
 
                val dataMap = mutableMapOf<String,Any>()
                dataMap[CHILD_ID] = uid
                dataMap[CHILD_PHONE] = mPhoneNumber
                dataMap[CHILD_USERNAME] = uid//
                REF_DATABASE_ROOT.child(NODE_PHONES).child(mPhoneNumber).setValue(uid)
                    .addOnFailureListener{ showToast("fail") }
                    . addOnSuccessListener {
                        REF_DATABASE_ROOT.child(NODE_USER).child(uid).updateChildren(dataMap)
                            .addOnSuccessListener {
                                restartActivity()
                                showToast("welcome")

                            }
                            .addOnFailureListener{
                                showToast("fail ")
                            }
                    }
            } else{
                showToast( task.exception?.message.toString())
            }
        }
    }
}

