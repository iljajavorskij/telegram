package com.example.mymassenger.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mymassenger.R
import com.example.mymassenger.databinding.FragmentChatsBinding
import com.example.mymassenger.utilits.APP_ACTIVITY
import com.example.mymassenger.utilits.hideOnKeyboard


class  MainFragment : Fragment(R.layout.fragment_chats) {


    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Telegram"
        APP_ACTIVITY.mAppDriwer.enableDrawer()
        hideOnKeyboard()

    }
}

