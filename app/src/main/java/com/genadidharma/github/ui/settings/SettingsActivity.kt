package com.genadidharma.github.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.genadidharma.github.R

class SettingsActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportFragmentManager.beginTransaction().add(R.id.setting_holder, MyPreferenceFragment()).commit()
    }
}