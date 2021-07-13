package com.example.mymassenger.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymassenger.R
import com.example.mymassenger.databinding.FragmentContactBinding
import com.example.mymassenger.models.CommonModel
import com.example.mymassenger.utilits.*
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView


class ContactFragment : BlankFragment(R.layout.fragment_contact) {

    lateinit var mBinding: FragmentContactBinding
    lateinit var mRecyclerView:RecyclerView
    lateinit var mAdapter:FirebaseRecyclerAdapter<CommonModel,ContactHolder>
    lateinit var mRefContacts:DatabaseReference
    lateinit var mUserRef:DatabaseReference
    private lateinit var mRefUsersListeber:AppValueEventListener//создаю лисенер
    val mapListener = hashMapOf<DatabaseReference,AppValueEventListener>()//создаю мапу которая принимает ключ ссылку а в значение лисенер

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentContactBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }


    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "контакты"
        initRecyclerView()//функция инициализации ресайклервью
    }

    private fun initRecyclerView() {
        mRecyclerView = mBinding.contactRecyclerView //инициализирую ресайклер вью
        mRefContacts = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACT).child(UID)//инициализирую путь к ноде контактов
        val options = FirebaseRecyclerOptions.Builder<CommonModel>()//создаю запрос устанавливаю ему путь и модель для принятия данных
            .setQuery(mRefContacts,CommonModel::class.java)
            .build()
        mAdapter = object :FirebaseRecyclerAdapter<CommonModel,ContactHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_item,parent,false)
                return ContactHolder(view)
            }

            override fun onBindViewHolder(
                holder: ContactHolder,
                position: Int,
                model: CommonModel
            ) {
                mUserRef = REF_DATABASE_ROOT.child(NODE_USER).child(model.id)


                //инициализирую лисенер
                mRefUsersListeber = AppValueEventListener {
                    val contact = it.getCommonModel()
                    if (contact.fullname.isEmpty()){
                        holder.name.text = contact.phone
                    }else{holder.name.text = contact.fullname}
                    holder.status.text = contact.state
                    holder.photo.dowloadAndSetImage(contact.photoUrl)
                }

                mUserRef.addValueEventListener(mRefUsersListeber)//вешаю слушатель на ссылку на рутовую ссылку
                mapListener[mUserRef] = mRefUsersListeber//передаю лисенер в мапу по ключу рутовой ссылки


            }
        }
        mRecyclerView.adapter = mAdapter
        mAdapter.startListening()
    }

    class ContactHolder(view: View):RecyclerView.ViewHolder(view){
         val name: TextView = view.findViewById(R.id.contact_fullname)
         val status: TextView = view.findViewById(R.id.contact_state)
         val photo: CircleImageView = view.findViewById(R.id.contact_photo)
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()//прохожусь циклом по мапе и на каждой итерации по ключу которым является ссылка к которой прикреплен слушатель удаляю удаляю слушатель позначению которым является слушатель
        mapListener.forEach { map ->
            map.key.removeEventListener(map.value)
        }
    }
}




