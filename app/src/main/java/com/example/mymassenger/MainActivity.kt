package com.example.mymassenger

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.inputmethodservice.InputMethodService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.mymassenger.activity.RegisterActivity
import com.example.mymassenger.databinding.ActivityMainBinding
import com.example.mymassenger.models.User
import com.example.mymassenger.ui.fragments.ChatsFragment
import com.example.mymassenger.ui.objects.AppDrawer
import com.example.mymassenger.utilits.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.theartofdev.edmodo.cropper.CropImage
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    lateinit var mAppDriwer: AppDrawer
    private lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        initFirebase()
        initUser{
            initFieleds()//функция для инициализации всех полей
            initFunc()//функция для фунцианальности активити
        }
    }
    private fun initFunc() {
        if (AUTH.currentUser != null) {
            setSupportActionBar(mToolbar)//устанавливаем тулбар
            mAppDriwer.create()
            replaceFragment(ChatsFragment())
        }else {
            replaceActivity(RegisterActivity())
        }
    }
    private fun initFieleds() {//для инициализации всех полей
        mToolbar = mBinding.mainToolBar//инициализируем тулбар
        mAppDriwer = AppDrawer(this,mToolbar)

    }
}