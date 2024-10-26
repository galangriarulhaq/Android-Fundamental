package com.bangkit.eventdicodingapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.bangkit.eventdicodingapp.R
import com.bangkit.eventdicodingapp.data.local.datastore.SettingPreferences
import com.bangkit.eventdicodingapp.data.local.datastore.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        loadThemeSetting()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1000)
    }


    private fun loadThemeSetting() {
        val pref = SettingPreferences.getInstance(application.dataStore)
        lifecycleScope.launch {
            val isDarkModeActive = pref.getThemeSetting().first()
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

}