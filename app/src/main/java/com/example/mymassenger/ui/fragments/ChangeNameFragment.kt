package com.example.mymassenger.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.mymassenger.MainActivity
import com.example.mymassenger.R
import com.example.mymassenger.databinding.FragmentChangeNameBinding
import com.example.mymassenger.utilits.*


class ChangeNameFragment : BaseChangeFragment(R.layout.fragment_change_name) {

    lateinit var mBinding: FragmentChangeNameBinding
    lateinit var mName:EditText
    lateinit var mSurname:EditText
    lateinit var fullname:String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentChangeNameBinding.inflate(layoutInflater,container,false)
        mName = mBinding.changeName
        mSurname = mBinding.changeSurname
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()

        var fullNameList:List<String> = USER.fullname.trim().split(" ")
        if (fullNameList.size>1){
            mName.setText(fullNameList[0])
            mSurname.setText(fullNameList[1])
        }else{
            mName.setText(fullNameList[0])
        }


    }


    override fun change() {
        val name = mName.text.toString()
        val surName = mSurname.text.toString()


        if (name.isEmpty()){

        }else{
            val fullName = "$name $surName"
            REF_DATABASE_ROOT//лбращаемся к базе по главвной ссыдке telegram-46746-default-rtdb
                .child(NODE_USER)//обращаемся к ноде юзерс следующей по дереву
                .child(UID)//обращаемся к уникальному идентификационному номену сделующему по дереву
                .child(CHILD_FULLNAME)//добавляем чайлд фуллнаме и присваиваем ему значение из переменной
                .setValue(fullName)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        USER.fullname = fullName
                        fragmentManager?.popBackStack()//устанавливаем обработчик события
                    }
                }
        }
     }
}