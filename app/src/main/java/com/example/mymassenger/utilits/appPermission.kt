package com.example.mymassenger.utilits

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

const val READ_CONTACT = Manifest.permission.READ_CONTACTS
const val RECORD_AUDIO = Manifest.permission.RECORD_AUDIO

const val REQUEST_CODE = 200//создал реквест код

fun checkPermission(permission: String):Boolean{
    return if (Build.VERSION.SDK_INT >= 23 &&//если сдк дольше чем 23 и разрешение небыло до этого предоставлено  то мы запрашиваем разрешение на доступ к контактам
         ContextCompat.checkSelfPermission(APP_ACTIVITY,permission) != PackageManager.PERMISSION_GRANTED){
             ActivityCompat.requestPermissions(APP_ACTIVITY, arrayOf(permission),REQUEST_CODE)//вызываю окно которое принимает активити массив с пермишенами и реквест код
            false
    }else true
}