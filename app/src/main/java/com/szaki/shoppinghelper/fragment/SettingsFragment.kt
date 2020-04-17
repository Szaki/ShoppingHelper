package com.szaki.shoppinghelper.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.widget.Toast
import com.szaki.shoppinghelper.R
import com.szaki.shoppinghelper.SettingsActivity
import com.szaki.shoppinghelper.data.Item
import com.szaki.shoppinghelper.data.SimpleItem
import com.szaki.shoppinghelper.dialog.AboutDialog
import com.szaki.shoppinghelper.dialog.ShopSettingsDialog
import com.szaki.shoppinghelper.other.FileHandler

class SettingsFragment : PreferenceFragmentCompat() {

    lateinit var fileHandler: FileHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileHandler = FileHandler(requireContext())
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        findPreference("stores").setOnPreferenceClickListener {
            startActivity(Intent(activity, ShopSettingsDialog::class.java))
            true
        }
        findPreference("default").setOnPreferenceClickListener {
            fileHandler.writeArrayList(fileHandler.readArrayList<Item>("items.ser"), "defaultitems.ser")
            fileHandler.uploadList("defaultitems.ser")
            Toast.makeText(activity, getString(R.string.itemdefaultset), Toast.LENGTH_SHORT).show()
            true
        }
        findPreference("cleardefault").setOnPreferenceClickListener {
            fileHandler.writeArrayList(ArrayList<Item>(), "defaultitems.ser")
            fileHandler.uploadList("defaultitems.ser")
            Toast.makeText(activity, getString(R.string.itemdefaultcleared), Toast.LENGTH_SHORT).show()
            true
        }
        findPreference("itemhistory").setOnPreferenceClickListener {
            fileHandler.writeArrayList(ArrayList<SimpleItem>(), "itemhistory.ser")
            Toast.makeText(activity, getString(R.string.itemhistorycleared), Toast.LENGTH_SHORT).show()
            true
        }
        findPreference("cathistory").setOnPreferenceClickListener {
            fileHandler.writeArrayList(ArrayList<SimpleItem>(), "cathistory.ser")
            Toast.makeText(activity, getString(R.string.cathistorycleared), Toast.LENGTH_SHORT).show()
            true
        }
        findPreference("about").setOnPreferenceClickListener {
            AboutDialog().show((context as SettingsActivity).supportFragmentManager, "About")
            true
        }
    }
}