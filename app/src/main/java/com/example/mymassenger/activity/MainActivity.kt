package com.example.mymassenger.activity

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.mymassenger.databinding.ActivityMainBinding
import com.example.mymassenger.ui.fragments.MainFragment
import com.example.mymassenger.ui.fragments.register.EnterPhoneNumber
import com.example.mymassenger.ui.objects.AppDrawer
import com.example.mymassenger.utilits.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    lateinit var mAppDriwer: AppDrawer
    lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        initFirebase()
        initUser{
            CoroutineScope(Dispatchers.IO).launch {
                initContacts()//вызываем функцию которая получает доступ к контактам
            }
            initFieleds()//функция для инициализации всех полей
            initFunc()//функция для фунцианальности активити
        }
    }



    private fun initFunc() {
        setSupportActionBar(mToolbar)//устанавливаем тулбар
        if (AUTH.currentUser != null) {

            mAppDriwer.create()
            replaceFragment(MainFragment())
        }else {
            replaceFragment(EnterPhoneNumber())
        }
    }
    private fun initFieleds() {//для инициализации всех полей
        mToolbar = mBinding.mainToolBar//инициализируем тулбар
        mAppDriwer = AppDrawer(this,mToolbar)

    }

    override fun onStart() {
        super.onStart()
        AppState.updateState(AppState.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppState.updateState(AppState.OFFLINE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(APP_ACTIVITY, READ_CONTACT) == PackageManager.PERMISSION_GRANTED){
            initContacts()
        }
    }
}