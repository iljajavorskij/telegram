package com.example.mymassenger.utilits

import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mymassenger.R
import com.example.mymassenger.activity.RegisterActivity
import com.example.mymassenger.ui.fragments.ChatsFragment
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

fun showToast(message: String){
    Toast.makeText(APP_ACTIVITY,message,Toast.LENGTH_SHORT).show()

}


fun AppCompatActivity.replaceActivity(activity: AppCompatActivity){
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()

}

fun AppCompatActivity.replaceFragment(fragment: Fragment){
    supportFragmentManager
        .beginTransaction()
        .addToBackStack(null)
        .replace(R.id.dataConteiner, fragment)
        .commit()


}fun Fragment.replaceFragment(fragment: Fragment){
    fragmentManager
        ?.beginTransaction()
        ?.addToBackStack(null)
        ?.replace(R.id.dataConteiner, fragment)
        ?.commit()
}

fun hideOnKeyboard(){//функция которая убирает клавиатуру
    val imm: InputMethodManager = APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(APP_ACTIVITY.window.decorView.windowToken,0)
}

fun ImageView.dowloadAndSetImage(url:String){
    val imag = "https://yandex.ru/images/search?pos=2&img_url=https%3A%2F%2Fbasik.ru%2Fimages%2Fmountains_wallpapers_2%2F20_mountains.jpg&text=%D0%BA%D0%B0%D1%80%D1%82%D0%B8%D0%BD%D0%BA%D0%B0&lr=2&rpt=simage&source=wiz"
    Picasso.get()
        .load(imag)
        .fit()
        .placeholder(R.drawable.default_photo)
        .into(this)
}