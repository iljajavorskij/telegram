package com.example.mymassenger.SinglChat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mymassenger.R
import com.example.mymassenger.models.CommonModel
import com.example.mymassenger.utilits.UID
import com.example.mymassenger.utilits.asTime
import kotlinx.android.synthetic.main.message_item.view.*

class SinglChatAdapter: RecyclerView.Adapter<SinglChatAdapter.SinglChatHolder>() {

    var mListMessage = emptyList<CommonModel>()

    class SinglChatHolder(view: View):RecyclerView.ViewHolder(view){
        val chatContainer:ConstraintLayout = view.singl_chat_container
        val chatMessage:TextView = view.chat_item_message
        val chatTime:TextView = view.chat_item_message_time

        val chatReceivedContainer:ConstraintLayout = view.singl_chat_receiver_container
        val receivedChatMessage: TextView = view.chat_received_message
        val receivedMessageTime:TextView = view.chat_received_message_time
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SinglChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item,parent,false)
        return SinglChatHolder(view)
    }

    override fun onBindViewHolder(holder: SinglChatHolder, position: Int) {
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

    fun setList(list: List<CommonModel>){
        mListMessage = list
        notifyDataSetChanged()
    }
}


