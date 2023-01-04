package com.lukman.githubuser.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.lukman.githubuser.Helper.SettingPreference
import com.lukman.githubuser.data.local.Entity.UserFavorite
import com.lukman.githubuser.data.local.Room.UserFavoriteDao
import com.lukman.githubuser.data.local.Room.UserFavoriteDatabase

class UserFavoriteRepository(application:Application) {
    private val userFavoriteDao:UserFavoriteDao
    init {
        val db = UserFavoriteDatabase.getInstance(application)
        userFavoriteDao = db.uFavoriteDao()
    }

    fun getUserFavorites():LiveData<List<UserFavorite>>{
        return userFavoriteDao.getUserFavorites()
    }

    suspend fun tambahUserKeFavorite(userFavorite: UserFavorite){
        userFavoriteDao.tambahUserKeFavorite(userFavorite)
    }

    suspend fun hapusUserDariFavorite(userFavorite:UserFavorite){
        userFavoriteDao.hapusDariFavorite(userFavorite)
    }

    suspend fun isUserFavorite(userId:Long):Boolean{
        return userFavoriteDao.IsUserFavorite(userId)
    }

    fun getThemeSetting(preference: SettingPreference):LiveData<Boolean>{
        return preference.getThemeSetting().asLiveData()
    }

    suspend fun saveThemeSetting(preference: SettingPreference, isDarkModeActive:Boolean){
        preference.saveThemeSetting(isDarkModeActive)
    }


}