package com.example.mymassenger.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mymassenger.R
import com.example.mymassenger.databinding.ActivityRegisterBinding
import com.example.mymassenger.ui.fragments.EnterPhoneNumber
import com.example.mymassenger.utilits.initFirebase
import com.example.mymassenger.utilits.replaceFragment

class RegisterActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initFirebase()
    }

    override fun onStart() {
        super.onStart()
        mToolbar = mBinding.registerToolBar
        setSupportActionBar(mToolbar)
        title = getString(R.string.your_phone)
        replaceFragment(EnterPhoneNumber())
    }

}