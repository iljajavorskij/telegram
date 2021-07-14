package com.example.mymassenger.utilits

import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mymassenger.MainActivity
import com.example.mymassenger.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

fun showToast(message: String){
    Toast.makeText(APP_ACTIVITY,message,Toast.LENGTH_SHORT).show()

}


fun restartActivity(){
    val intent = Intent(APP_ACTIVITY, MainActivity::class.java)
    APP_ACTIVITY.startActivity(intent)
    APP_ACTIVITY.finish()
}

fun replaceFragment(fragment: Fragment){
    APP_ACTIVITY.supportFragmentManager
        .beginTransaction()
        .addToBackStack(null)
        .replace(R.id.dataConteiner, fragment)
        .commit()


}

fun hideOnKeyboard(){//функция которая убирает клавиатуру
    val imm: InputMethodManager = APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(APP_ACTIVITY.window.decorView.windowToken,0)
}

fun ImageView.dowloadAndSetImage(url:String){
    Picasso.get()
        .load("https://yandex.ru/images/search?pos=7&img_url=https%3A%2F%2Fscontent-hel3-1.cdninstagram.com%2Fv%2Ft51.2885-15%2Fe35%2Fs1080x1080%2F120087564_248084696528413_7782036935397588027_n.jpg%3F_nc_ht%3Dscontent-hel3-1.cdninstagram.com%26_nc_cat%3D101%26_nc_ohc%3DqH68mTxyst8AX-8iVoB%26_nc_tp%3D15%26oh%3D5f8ca57d4d40cc34053c08de3ca6c337%26oe%3D5FA84979&text=%D0%BA%D0%B0%D1%80%D1%82%D0%B8%D0%BD%D0%BA%D0%B0&lr=2&rpt=simage&source=wiz")
        .fit()
        .placeholder(R.drawable.default_photo)
        .into(this)
}


fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}