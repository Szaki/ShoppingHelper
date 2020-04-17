package com.szaki.shoppinghelper

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.szaki.shoppinghelper.fragment.SettingsFragment

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settingsframe, SettingsFragment())
                .commit()
    }
}
