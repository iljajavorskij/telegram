package com.example.mymassenger.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.mymassenger.R
import com.example.mymassenger.databinding.FragmentChatsBinding
import com.example.mymassenger.databinding.FragmentSettingsBinding


class SettingsFragment : BlankFragment(R.layout.fragment_settings) {

    override fun onResume() {
        super.onResume()

        setHasOptionsMenu(true)



    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.setting_menu_up,menu)

    }
}
