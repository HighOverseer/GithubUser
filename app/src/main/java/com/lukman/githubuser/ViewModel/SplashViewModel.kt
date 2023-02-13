package com.lukman.githubuser.ViewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lukman.githubuser.Helper.SettingPreference
import com.lukman.githubuser.data.UserFavoriteRepository

class SplashViewModel(application: Application):ViewModel() {
    private val userFavoriteRepository = UserFavoriteRepository(application)

}