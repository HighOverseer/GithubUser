package com.lukman.githubuser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.lukman.githubuser.Helper.SettingPreference
import com.lukman.githubuser.Helper.ViewModelFactory
import com.lukman.githubuser.ViewModel.SplashViewModel
import com.lukman.githubuser.databinding.ActivitySplashBinding


class Splash : AppCompatActivity() {
    private val Context.dataStore:DataStore<Preferences> by preferencesDataStore(name = "Setting")
    private var _binding:ActivitySplashBinding?=null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()
        val handler = Handler(Looper.getMainLooper())
        val pref = SettingPreference.getInstance(dataStore)
        val splashViewModel:SplashViewModel = obtainViewModel(this)
        splashViewModel.getThemeSetting(pref).observe(this){ isDarkModeActive ->
            if(isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding?.root?.background = AppCompatResources.getDrawable(this, R.drawable.bg_black)

            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding?.root?.background = AppCompatResources.getDrawable(this, R.drawable.bg_grey)
            }
            handler.postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, tDelay)
        }


    }

    private fun obtainViewModel(activity: AppCompatActivity): SplashViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[SplashViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val tDelay = 2000L
    }
}