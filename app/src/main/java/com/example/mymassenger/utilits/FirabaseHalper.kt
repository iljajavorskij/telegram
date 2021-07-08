package com.example.mymassenger.utilits

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

lateinit var AUTH:FirebaseAuth//эта переменная инициализируется один раз в мэйн активити и работает во всем приложении
lateinit var REF_DATABASE_ROOT:DatabaseReference

const val NODE_USER = "user"
const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"

fun initFirebase(){
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
}