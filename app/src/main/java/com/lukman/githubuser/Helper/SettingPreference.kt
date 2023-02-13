package com.lukman.githubuser.Helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SettingPreference private constructor(private val dataStore: DataStore<Preferences>) {


    fun getThemeSetting():Flow<Boolean>{
        return dataStore.data.map { preference ->
            preference[THEME_KEY]?:false
        }
    }

    suspend fun getThemeSettingNonFlow():Preferences{
        return dataStore.data.first()
    }

    suspend fun saveThemeSetting(isDarkModeActive:Boolean){
        dataStore.edit { preference ->
            preference[THEME_KEY] = isDarkModeActive
        }
    }

    companion object{
        @Volatile
        private var INSTANCE:SettingPreference?=null

        val THEME_KEY = booleanPreferencesKey("theme_setting")

        fun getInstance(dataStore: DataStore<Preferences>):SettingPreference{
            return INSTANCE?: synchronized(this){
                val instance = SettingPreference(dataStore)
                INSTANCE = instance
                instance
            }

        }
    }
}