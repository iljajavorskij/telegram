package com.example.mymassenger.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import com.example.mymassenger.R
import com.example.mymassenger.databinding.FragmentSinglChatBinding
import com.example.mymassenger.models.CommonModel
import com.example.mymassenger.models.User
import com.example.mymassenger.utilits.*
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.toolbar_info.view.*


class SinglChatFragment(private val contact: CommonModel) : Fragment(R.layout.fragment_singl_chat) {
    private lateinit var mBinding:FragmentSinglChatBinding
    private lateinit var mToolbarInfo:View
    private lateinit var mListener:AppValueEventListener
    private lateinit var mReceivingUser:User
    private lateinit var mRefUser:DatabaseReference



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSinglChatBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }



    override fun onResume() {
        super.onResume()
        mToolbarInfo = APP_ACTIVITY.mToolbar.toolbar_info
        mToolbarInfo.visibility = View.VISIBLE
        mListener = AppValueEventListener {
            mReceivingUser = it.getUserModel()
            initToolbarInfo()
        }
        mRefUser = REF_DATABASE_ROOT.child(NODE_USER).child(contact.id)
        mRefUser.addValueEventListener(mListener)
    }

    private fun initToolbarInfo() {
        mToolbarInfo.contact_photo_toolbar.dowloadAndSetImage(mReceivingUser.photoUrl)
        mToolbarInfo.contact_fullname_toolbar.text  = mReceivingUser.fullname
        mToolbarInfo.contact_state_toolbar.text = mReceivingUser.state
    }

    override fun onPause() {
        super.onPause()
        APP_ACTIVITY.mToolbar.toolbar_info.visibility = View.GONE
        mRefUser.removeEventListener(mListener)

    }
}