package com.szaki.shoppinghelper

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.szaki.shoppinghelper.data.Shop
import com.szaki.shoppinghelper.dialog.DialogListener
import com.szaki.shoppinghelper.fragment.ContactListFragment
import com.szaki.shoppinghelper.fragment.ItemListFragment
import com.szaki.shoppinghelper.fragment.ShopListFragment
import com.szaki.shoppinghelper.other.FileHandler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DialogListener {
    val itemFragment = ItemListFragment()
    val shopFragment = ShopListFragment()
    val contactFragment = ContactListFragment()
    val fileHandler = FileHandler(this)
    var shops = 0
    lateinit var ft: FragmentTransaction


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_stores -> {
                if (shops == 0) {
                    Toast.makeText(this, getString(R.string.shoperror), Toast.LENGTH_LONG).show()
                    return@OnNavigationItemSelectedListener false
                } else {
                    ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.frame, shopFragment)
                    ft.commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            R.id.nav_list -> {
                ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.frame, itemFragment)
                ft.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_notification -> {
                ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.frame, contactFragment)
                ft.commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.nav_list
    }

    override fun onResume() {
        shops = fileHandler.readArrayList<Shop>("shops.ser").size
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onEIDPositiveClick(dialog: DialogFragment) {
        itemFragment.onEIDPositiveClick(dialog)
    }

    override fun onAIDPositiveClick(dialog: DialogFragment) {
        itemFragment.onAIDPositiveClick(dialog)
    }
}
