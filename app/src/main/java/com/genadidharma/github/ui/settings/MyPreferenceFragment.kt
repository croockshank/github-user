package com.genadidharma.github.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.genadidharma.github.R
import com.genadidharma.github.receiver.AlarmReceiver
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MyPreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var NOTIFY: String

    private lateinit var isNotifyPreference: SwitchPreference

    private val alarmReceiver = AlarmReceiver()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
        setSummaries()
    }

    private fun init() {
        NOTIFY = resources.getString(R.string.key_notify)

        isNotifyPreference = findPreference<SwitchPreference>(NOTIFY) as SwitchPreference
    }

    private fun setSummaries() {
        val sharedPreferences = preferenceManager.sharedPreferences
        isNotifyPreference.isChecked = sharedPreferences.getBoolean(NOTIFY, false)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == NOTIFY) {
            isNotifyPreference.isChecked = sharedPreferences?.getBoolean(NOTIFY, false) ?: false
            toggleNotification(isNotifyPreference.isChecked)
        }
    }

    private fun toggleNotification(isChecked: Boolean){
        if(isChecked){
            val repeatTime = "09:00"
            val repeatTimeMessage = "Let's Looking for Another Talented Programmer Out There!"
            context?.let {
                alarmReceiver.setRepeatingAlarm(it, AlarmReceiver.TYPE_REPEATING, repeatTime, repeatTimeMessage)
            }
        }else{
            context?.let {
                alarmReceiver.cancelAlarm(it, AlarmReceiver.TYPE_REPEATING)
            }
        }
    }
}