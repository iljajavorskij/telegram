package com.example.mymassenger.SinglChat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mymassenger.R
import com.example.mymassenger.databinding.FragmentSinglChatBinding
import com.example.mymassenger.models.CommonModel
import com.example.mymassenger.models.User
import com.example.mymassenger.utilits.*
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_singl_chat.*
import kotlinx.android.synthetic.main.toolbar_info.view.*


class SinglChatFragment(private val contact: CommonModel) : Fragment(R.layout.fragment_singl_chat) {
    private lateinit var mBinding:FragmentSinglChatBinding
    private lateinit var mToolbarInfo:View
    private lateinit var mListener:AppValueEventListener
    private lateinit var mReceivingUser:User
    private lateinit var mRefUser:DatabaseReference
    private lateinit var mRefMessages:DatabaseReference
    private lateinit var mRecyclerView:RecyclerView
    private lateinit var mAdapter:SinglChatAdapter
    private lateinit var mMessageListener:AppValueEventListener
    private var mListMessges = emptyList<CommonModel>()



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
        initToolBar()
        initRecyclerView()

    }

    private fun initRecyclerView() {
        mRecyclerView = chat_recyclerview
        mAdapter = SinglChatAdapter()
        mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UID).child(contact.id)
        mRecyclerView.adapter = mAdapter
        mMessageListener = AppValueEventListener { snap ->
            mListMessges = snap.children.map { it.getCommonModel() }
            mAdapter.setList(mListMessges)
            mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
        }
        mRefMessages.addValueEventListener(mMessageListener)
    }


    private fun initToolBar(){
    mToolbarInfo = APP_ACTIVITY.mToolbar.toolbar_info
    mToolbarInfo.visibility = View.VISIBLE
    mListener = AppValueEventListener {
        mReceivingUser = it.getUserModel()
        initToolbarInfo()
    }
    mRefUser = REF_DATABASE_ROOT.child(NODE_USER).child(contact.id)
    mRefUser.addValueEventListener(mListener)
    chat_send_imageView.setOnClickListener{
        val message = chat_editText.text.toString()
        if (message.isEmpty()){
            showToast("введите ссобщение")
        }else{
            sendMessage(message,contact.id,TYPE_TEXT){
                chat_editText.setText("")
            }
        }
    }
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
        mRefMessages.removeEventListener(mMessageListener)

    }
}