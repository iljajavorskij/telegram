package com.example.mymassenger.SinglChat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mymassenger.R
import com.example.mymassenger.databinding.FragmentSinglChatBinding
import com.example.mymassenger.models.CommonModel
import com.example.mymassenger.models.User
import com.example.mymassenger.utilits.*
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
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
    private lateinit var mMessageListener:AppChildEventListener
    private var mCountMessages = 15
    private var mIsScrilling = false
    private var mSmoothScroller = true
    private lateinit var mSwipeRefresh:SwipeRefreshLayout
    private lateinit var mLayoutManager:LinearLayoutManager




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
        initFields()
        initToolBar()
        initRecyclerView()


    }

    private fun initFields() {
        mSwipeRefresh = chat_swipe_layout
        mLayoutManager = LinearLayoutManager(this.context)
        chat_editText.addTextChangedListener(AppTextWatcher{
            val strihg = chat_editText.text.toString()
            if (strihg.isEmpty()){
                chat_send_imageView.visibility = View.GONE
                chat_send_attach.visibility = View.VISIBLE
            }else{
                chat_send_imageView.visibility = View.VISIBLE
                chat_send_attach.visibility = View.GONE
            }
        })

        chat_send_attach.setOnClickListener{
            attachFile()
        }
    }

    private fun attachFile() {
        CropImage.activity()
            .setAspectRatio(1,1)
            .setRequestedSize(250,250)
            .start(APP_ACTIVITY,this)
    }


    private fun initRecyclerView() {
        mRecyclerView = chat_recyclerview
        mAdapter = SinglChatAdapter()
        mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UID).child(contact.id)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.isNestedScrollingEnabled = false

        mMessageListener = AppChildEventListener{
            var message = it.getCommonModel()
            if (mSmoothScroller){
                mAdapter.addItemToBottom(message){
                    mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
                }
            }else{
                mAdapter.addItemToTop(message){
                    mSwipeRefresh.isRefreshing = false
                }
            }


        }
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessageListener)//метод лимит то ласт загружает в адаптер только 10 послдних айтемов

        mRecyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    mIsScrilling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrilling && dy < 0 && mLayoutManager.findFirstVisibleItemPosition() <= 3){
                    updateData()
                }
            }

        })
        mSwipeRefresh.setOnRefreshListener { updateData() }
    }

    private fun updateData() {//подгружаю еще 10 элементов
        mSmoothScroller = false//не пролистывает элементы в начало при дозагрузке
        mIsScrilling = false
        mCountMessages += 10
        mRefMessages.removeEventListener(mMessageListener )
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessageListener)
        
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
        mSmoothScroller = true
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK && data != null) {
            val uri =
                CropImage.getActivityResult(data).uri//получаем ури области которая обрезана из активитирезалт
            val messageKey = REF_DATABASE_ROOT
                .child(NODE_MESSAGES)
                .child(UID)
                .child(contact.id)
                .push().key.toString()
            val path = REF_STORAGE_ROOT
                .child(FOLDER_MESSAGES_IMAGES)
                .child(messageKey)//создаю путь

            putImageToStorge(uri, path) {
                getUrlFromStirage(path) {
                    putUrlToDatabase(it) {
                        sendMessageAsImage(contact.id,it,messageKey)
                        mSmoothScroller = true
                    }
                }
            }
        }
    }



    override fun onPause() {
        super.onPause()
        APP_ACTIVITY.mToolbar.toolbar_info.visibility = View.GONE
        mRefUser.removeEventListener(mListener)
        mRefMessages.removeEventListener(mMessageListener)
    }
}

