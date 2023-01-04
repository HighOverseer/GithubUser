package com.lukman.githubuser.ViewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lukman.githubuser.data.UserFavoriteRepository
import com.lukman.githubuser.data.local.Entity.UserFavorite

class FavoriteViewModel(application: Application):ViewModel() {
    private val userFavoriteRepository = UserFavoriteRepository(application)

    fun getUserFavorites():LiveData<List<UserFavorite>>{
        return userFavoriteRepository.getUserFavorites()
    }
}