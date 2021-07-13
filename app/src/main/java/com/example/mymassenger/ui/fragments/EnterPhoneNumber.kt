package com.example.mymassenger.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.mymassenger.MainActivity
import com.example.mymassenger.R
import com.example.mymassenger.activity.RegisterActivity
import com.example.mymassenger.databinding.FragmentEnterPhoneNumberBinding
import com.example.mymassenger.utilits.AUTH
import com.example.mymassenger.utilits.replaceActivity
import com.example.mymassenger.utilits.replaceFragment
import com.example.mymassenger.utilits.showToast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class EnterPhoneNumber : BlankFragment(R.layout.fragment_enter_phone_number) {


    private lateinit var mBinding:  FragmentEnterPhoneNumberBinding//сщздаю связку
    private lateinit var mButton: FloatingActionButton
    private lateinit var mRegisterPhone:EditText
    private lateinit var mPhoneNumber: String
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks//поле колбэка для идентификации


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentEnterPhoneNumberBinding.inflate(layoutInflater,container,false)
        return mBinding.root

    }

    override fun onResume() {
        super.onResume()
        mButton = mBinding.registerBtnNext
        mRegisterPhone = mBinding.registerNumber
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){//создаю колбэк
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {//этот метод выполнется если верификация прошла успешно
            AUTH.signInWithCredential(credential).addOnCompleteListener { task ->//если пользователь найден то запускаю мэйн активити вместо регист
                if (task.isSuccessful){
                    showToast("добро поаловать")
                    (activity as RegisterActivity).replaceActivity(MainActivity())
                } else{
                    showToast( task.exception?.message.toString())
                }
            }
        }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(FragmentEnterCode(mPhoneNumber,id))
            }

            override fun onVerificationFailed(p0: FirebaseException) {//метод вызывается если верификация провалена
                showToast(p0.message.toString() )//вывожу сообщение об ошибке
            }

        }
        mButton.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        if (mRegisterPhone.text.toString().isEmpty()){
            showToast("Ok")
        }else {
            authUser()
        }
    }

    private fun authUser() {
        mPhoneNumber = mRegisterPhone.text.toString().trim()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mPhoneNumber,60,TimeUnit.SECONDS,activity as RegisterActivity,mCallback)
    }
}