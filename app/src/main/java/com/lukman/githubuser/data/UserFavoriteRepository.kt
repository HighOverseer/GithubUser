package com.lukman.githubuser.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.lukman.githubuser.Helper.SettingPreference
import com.lukman.githubuser.data.local.Entity.UserFavorite
import com.lukman.githubuser.data.local.Room.UserFavoriteDao
import com.lukman.githubuser.data.local.Room.UserFavoriteDatabase
import androidx.datastore.preferences.core.Preferences

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

    suspend fun getThemeSetting(preference: SettingPreference):Preferences{
        return preference.getThemeSettingNonFlow()
    }

    suspend fun saveThemeSetting(preference: SettingPreference, isDarkModeActive:Boolean){
        preference.saveThemeSetting(isDarkModeActive)
    }


}