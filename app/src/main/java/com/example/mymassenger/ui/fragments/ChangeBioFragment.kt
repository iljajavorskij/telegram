package com.example.mymassenger.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mymassenger.R
import com.example.mymassenger.databinding.FragmentChangeBioBinding
import com.example.mymassenger.utilits.*


class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {

private lateinit var mBinding:FragmentChangeBioBinding
private lateinit var mtextBio:TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentChangeBioBinding.inflate(layoutInflater,container,false)
        mtextBio = mBinding.changeBio
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        mtextBio.text = USER.bio
    }

    override fun change() {
        super.change()
        val newBio = mtextBio.text.toString()
        REF_DATABASE_ROOT
            .child(NODE_USER)
            .child(UID).
            child(CHILD_BIO)
            .setValue(newBio)
            .addOnCompleteListener {
            if (it.isSuccessful){
                showToast("ok")
                 USER.bio = newBio
                 fragmentManager?.popBackStack()
            }
        }
    }
}