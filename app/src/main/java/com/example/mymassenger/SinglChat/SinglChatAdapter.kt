package com.example.mymassenger.SinglChat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mymassenger.R
import com.example.mymassenger.models.CommonModel
import com.example.mymassenger.utilits.*
import com.google.gson.annotations.Until
import kotlinx.android.synthetic.main.message_item.view.*

class SinglChatAdapter: RecyclerView.Adapter<SinglChatAdapter.SinglChatHolder>() {

    var mListMessage = mutableListOf<CommonModel>()
    private lateinit var mDiffResult:DiffUtil.DiffResult

    class SinglChatHolder(view: View):RecyclerView.ViewHolder(view){
        //text
        val chatContainer:ConstraintLayout = view.singl_chat_container
        val chatMessage:TextView = view.chat_item_message
        val chatTime:TextView = view.chat_item_message_time

        val chatReceivedContainer:ConstraintLayout = view.singl_chat_receiver_container
        val receivedChatMessage: TextView = view.chat_received_message
        val receivedMessageTime:TextView = view.chat_received_message_time

        //image
        val blockReceivedImage :ConstraintLayout = view.singl_chat_image_receiver_container
        val blockUserImage :ConstraintLayout = view.singl_chat_image_user_container
        val receivedImage :ImageView = view.received_image
        val userImage :ImageView = view.user_image
        val receivedTime :TextView = view.chat_received_image_message_time
        val userTime :TextView = view.chat_user_image_message_time



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SinglChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item,parent,false)
        return SinglChatHolder(view)
    }

    override fun onBindViewHolder(holder: SinglChatHolder, position: Int) {
        when(mListMessage[position].type){
            TYPE_MESSAGE_IMAGE -> drawMessageImage(holder,position)
            TYPE_MESSAGE_TEXT -> drawMessageText(holder,position)
        }


    }

    private fun drawMessageImage(holder: SinglChatAdapter.SinglChatHolder, position: Int) {
        holder.chatContainer.visibility = View.GONE
        holder.chatReceivedContainer.visibility = View.GONE
        if (mListMessage[position].from == UID){
            holder.blockReceivedImage.visibility = View.GONE
            holder.blockUserImage.visibility = View.VISIBLE
            holder.userImage.dowloadAndSetImage(mListMessage[position].photoUrl)
            holder.userTime.text = mListMessage[position].timeStamp.toString().asTime()

        }else{
            holder.chatContainer.visibility = View.GONE
            holder.chatReceivedContainer.visibility = View.GONE
            holder.blockReceivedImage.visibility = View.VISIBLE
            holder.blockUserImage.visibility = View.GONE
            holder.receivedImage.dowloadAndSetImage(mListMessage[position].photoUrl)
            holder.receivedTime.text = mListMessage[position].timeStamp.toString().asTime()
        }
    }

    private fun drawMessageText(holder: SinglChatAdapter.SinglChatHolder, position: Int) {
        holder.blockReceivedImage.visibility = View.GONE
        holder.blockUserImage.visibility = View.GONE
        if (mListMessage[position].from == UID){
            holder.chatContainer.visibility = View.VISIBLE
            holder.chatReceivedContainer.visibility = View.GONE
            holder.chatMessage.text = mListMessage[position].text
            holder.chatTime.text = mListMessage[position].timeStamp
                .toString().asTime()
        }else{
            holder.chatContainer.visibility = View.GONE
            holder.chatReceivedContainer.visibility = View.VISIBLE
            holder.receivedChatMessage.text = mListMessage[position].text
            holder.receivedMessageTime.text = mListMessage[position].timeStamp
                .toString().asTime()
        }
    }

    override fun getItemCount(): Int = mListMessage.size



fun addItemToBottom(item:CommonModel
                    ,onSuccess:() -> Unit){
    if (!mListMessage.contains(item)) {
        mListMessage.add(item)
        notifyItemInserted(mListMessage.size)
    }
    onSuccess()
}


    fun addItemToTop(item:CommonModel
                    ,onSuccess:() -> Unit){
        if (!mListMessage.contains(item)) {
            mListMessage.add(item)
            mListMessage.sortBy { it.timeStamp.toString() }
            notifyItemInserted(0)
        }
    onSuccess()
    }
}


