package com.example.mymassenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.mymassenger.activity.RegisterActivity
import com.example.mymassenger.databinding.ActivityMainBinding
import com.example.mymassenger.ui.fragments.ChatsFragment
import com.example.mymassenger.ui.objects.AppDrawer
import com.example.mymassenger.utilits.AUTH
import com.example.mymassenger.utilits.initFirebase
import com.example.mymassenger.utilits.replaceActivity
import com.example.mymassenger.utilits.replaceFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    lateinit var mAppDriwer: AppDrawer
    private lateinit var mToolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    @Override
    override fun onStart() {
        super.onStart()
        initFieleds()//функция для инициализации всех полей
        initFunc()//функция для фунцианальности активити
    }

    private fun initFunc() {
        if (AUTH.currentUser != null) {
            setSupportActionBar(mToolbar)//устанавливаем тулбар
            mAppDriwer.create()
            replaceFragment(ChatsFragment())
        }else{
            replaceActivity(RegisterActivity())

        }


    }

    private fun initFieleds() {//для инициализации всех полей
        mToolbar = mBinding.mainToolBar//инициализируем тулбар
        mAppDriwer = AppDrawer(this,mToolbar)
        initFirebase()
    }
}