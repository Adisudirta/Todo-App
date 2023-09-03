package com.dicoding.todoapp.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.*
import com.dicoding.todoapp.R
import com.dicoding.todoapp.notification.NotificationWorker
import com.dicoding.todoapp.utils.NOTIFICATION_CHANNEL_ID

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val prefNotification = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
            prefNotification?.setOnPreferenceChangeListener { preference, newValue ->
                val channelName = getString(R.string.notify_channel_name)
                //TODO 13 : Schedule and cancel daily reminder using WorkManager with data channelName
                if (newValue == true) {
                    scheduleDailyReminder(channelName)
                } else {
                    cancelDailyReminder()
                }

                true
            }

        }

        private fun scheduleDailyReminder(channelName: String) {
            val workManager = WorkManager.getInstance(requireContext())

            val inputData = Data.Builder()
                .putString(NOTIFICATION_CHANNEL_ID, channelName)
                .build()

            val dailyReminderRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInputData(inputData)
                .build()

            workManager.enqueueUniqueWork(
                "daily_reminder",
                ExistingWorkPolicy.REPLACE,
                dailyReminderRequest
            )
        }

        private fun cancelDailyReminder() {
            val workManager = WorkManager.getInstance(requireContext())
            workManager.cancelUniqueWork("daily_reminder")
        }

        private fun updateTheme(mode: Int): Boolean {
            AppCompatDelegate.setDefaultNightMode(mode)
            requireActivity().recreate()
            return true
        }
    }
}