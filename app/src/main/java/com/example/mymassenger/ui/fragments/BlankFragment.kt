package com.example.mymassenger.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mymassenger.utilits.APP_ACTIVITY


open class BlankFragment(var layout: Int ) : Fragment(layout) {
    lateinit var rootView:View


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(layout,container,false)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "чаты"
    }

    override fun onStart() {
        super.onStart()
//        (APP_ACTIVITY).mAppDriwer.disableDrawer()
    }

    override fun onStop() {
        super.onStop()
//        (APP_ACTIVITY).mAppDriwer.enableDrawer()
    }


}