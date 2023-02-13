package com.lukman.githubuser

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

import com.lukman.githubuser.databinding.ActivitySplashBinding


class Splash : AppCompatActivity() {
    private var _binding:ActivitySplashBinding?=null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this@Splash, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, tDelay)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val tDelay = 2000L
    }
}