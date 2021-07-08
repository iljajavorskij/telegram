package com.example.mymassenger.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mymassenger.MainActivity
import com.example.mymassenger.R


open class BlankFragment(var layout: Int ) : Fragment() {

    private lateinit var mRootView: View

    override fun onCreateView(inflater: LayoutInflater
                              , container: ViewGroup?
                              , savedInstanceState: Bundle?)
    : View? {

        mRootView = inflater.inflate(layout,container,false)

        return mRootView
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).mAppDriwer.disableDrawer()
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).mAppDriwer.enableDrawer()
    }


}