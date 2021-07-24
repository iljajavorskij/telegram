package com.example.mymassenger.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.EditText
import com.example.mymassenger.activity.MainActivity
import com.example.mymassenger.R
import com.example.mymassenger.databinding.FragmentChangeNameBinding
import com.example.mymassenger.utilits.*


class ChangeNameFragment : BaseChangeFragment(R.layout.fragment_change_name) {

    lateinit var mBinding: FragmentChangeNameBinding
    lateinit var mName: EditText
    lateinit var mSurname: EditText
    lateinit var fullname: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentChangeNameBinding.inflate(layoutInflater, container, false)
        mName = mBinding.changeName
        mSurname = mBinding.changeSurname
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()

        var fullNameList: List<String> = USER.fullname.trim().split(" ")
        if (fullNameList.size > 1) {
            mName.setText(fullNameList[0])
            mSurname.setText(fullNameList[1])
        } else {
            mName.setText(fullNameList[0])
        }


    }


    override fun change() {
        val name = mName.text.toString()
        val surName = mSurname.text.toString()

        if (name.isEmpty()) {
        } else {
            val fullName = "$name $surName"
            setNameToDatabase(fullName)
        }
    }
}