package com.example.mymassenger.ui.fragments

import android.view.*
import androidx.fragment.app.Fragment
import com.example.mymassenger.activity.MainActivity
import com.example.mymassenger.R
import com.example.mymassenger.utilits.hideOnKeyboard

open class BaseChangeFragment (layout: Int): Fragment(layout) {

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        (activity as MainActivity).mAppDriwer.disableDrawer()

    }

    override fun onStop() {
        super.onStop()
        hideOnKeyboard() 
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.menu_sessing_change_name,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.change_name_ok -> change()

        }
        return true
    }

    open fun change() {

    }
}