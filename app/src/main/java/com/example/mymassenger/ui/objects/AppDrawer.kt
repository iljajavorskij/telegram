package com.example.mymassenger.ui.objects

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ActionBarOverlayLayout
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mymassenger.MainActivity
import com.example.mymassenger.R
import com.example.mymassenger.ui.fragments.SettingsFragment
import com.example.mymassenger.utilits.replaceFragment
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

class  AppDrawer (val mainActivity: AppCompatActivity,val toolbar: Toolbar ){
    lateinit var mDrawer: Drawer
    private lateinit var mHeader: AccountHeader
    private lateinit var mDrawerLayout: DrawerLayout


    fun create(){
        createHeader()
        createDrawer()
        mDrawerLayout = mDrawer.drawerLayout
    }

    fun disableDrawer(){
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false//выключаю стандартрый тогл бургер
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)//включаю поведение кнопки назад для бургера
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)//блокирует драйвер в закрытом состоянии
        toolbar.setNavigationOnClickListener {
            mainActivity.supportFragmentManager.popBackStack()
        }

    }

    fun enableDrawer(){
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)//включаю поведение кнопки назад для бургера
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true//выключаю стандартрый тогл бургер
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)//блокирует драйвер в закрытом состоянии
        toolbar.setNavigationOnClickListener {
            mDrawer.openDrawer()
        }
    }
    private fun createDrawer(){
        mDrawer = DrawerBuilder()
                .withActivity(mainActivity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withSelectedItem(-1)
                .withAccountHeader(mHeader)
                .addDrawerItems(
                        PrimaryDrawerItem()
                                .withIdentifier(100)
                                .withName("создать группу")
                                .withIconTintingEnabled(true)
                                .withSelectable(true)
                                .withIcon(R.drawable.ic_menu_create_groups),
                        PrimaryDrawerItem()
                                .withIdentifier(101)
                                .withName("создать снкретный чат")
                                .withIconTintingEnabled(true)
                                .withSelectable(true)
                                .withIcon(R.drawable.ic_menu_secret_chat),
                        PrimaryDrawerItem()
                                .withIdentifier(102)
                                .withName("создать канал")
                                .withIconTintingEnabled(true)
                                .withSelectable(true)
                                .withIcon(R.drawable.ic_menu_create_channel),
                        PrimaryDrawerItem()
                                .withIdentifier(103)
                                .withName("котакты")
                                .withIconTintingEnabled(true)
                                .withSelectable(true)
                                .withIcon(R.drawable.ic_menu_contacts),
                        PrimaryDrawerItem()
                                .withIdentifier(104)
                                .withName("звонки")
                                .withIconTintingEnabled(true)
                                .withSelectable(true)
                                .withIcon(R.drawable.ic_menu_phone),
                        PrimaryDrawerItem()
                                .withIdentifier(105)
                                .withName("избранное")
                                .withIconTintingEnabled(true)
                                .withSelectable(true)
                                .withIcon(R.drawable.ic_menu_favorites),
                        PrimaryDrawerItem()
                                .withIdentifier(106)
                                .withName("настройки")
                                .withIconTintingEnabled(true)
                                .withSelectable(true)
                                .withIcon(R.drawable.ic_menu_settings),
                        DividerDrawerItem(),
                        PrimaryDrawerItem()
                                .withIdentifier(107)
                                .withName("Пригласить друзей")
                                .withIconTintingEnabled(true)
                                .withSelectable(true)
                                .withIcon(R.drawable.ic_menu_invate),
                        PrimaryDrawerItem()
                                .withIdentifier(108)
                                .withName("О нас")
                                .withIconTintingEnabled(true)
                                .withSelectable(true)
                                .withIcon(R.drawable.ic_menu_help)
                ).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener{
                    override fun onItemClick(
                            view: View?,
                            position: Int,
                            drawerItem: IDrawerItem<*>
                    ): Boolean {
                        when (position ){
                            7 -> mainActivity.replaceFragment(SettingsFragment())
                        }
                        return false
                    }
                }).build()
    }

    private fun createHeader() {
        mHeader = AccountHeaderBuilder()
                .withActivity(mainActivity)
                .withHeaderBackground(R.drawable.heder)
                .addProfiles(ProfileDrawerItem()
                        .withName("ilja javorskij")
                        .withEmail("89650647892")
                ).build()
    }
}