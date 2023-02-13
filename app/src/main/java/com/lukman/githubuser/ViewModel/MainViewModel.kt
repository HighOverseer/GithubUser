package com.lukman.githubuser.ViewModel

import android.app.Application
import androidx.lifecycle.*
import com.lukman.githubuser.Helper.SettingPreference
import com.lukman.githubuser.data.remote.ApiClient.API
import com.lukman.githubuser.ItemSearchUser
import com.lukman.githubuser.SearchUser
import com.lukman.githubuser.SingleEvent.Event
import com.lukman.githubuser.data.UserFavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application, settingPreference: SettingPreference) : ViewModel() {
    private val userFavoriteRepository = UserFavoriteRepository(application)

    private val _listUsers = MutableLiveData<ArrayList<ItemSearchUser>>()
    val listUsers: LiveData<ArrayList<ItemSearchUser>> = _listUsers

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _query = MutableLiveData<String>()
    val query: LiveData<String> = _query

    private val _isDarkModeActive = MutableLiveData<Boolean>()
    val isDarkModeActive:LiveData<Boolean> = _isDarkModeActive

    fun getListUsersOnSearchBar(username: String) {
        _isLoading.value = true
        _query.value = username
        val listUsersOnSearch = API.getApiService().getAllUsers(username)
        listUsersOnSearch.enqueue(object : Callback<SearchUser> {
            override fun onResponse(call: Call<SearchUser>, response: Response<SearchUser>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    isRvAllowedToDisplay = true
                    _listUsers.postValue(response.body()?.items as ArrayList<ItemSearchUser>)
                }
            }

            override fun onFailure(call: Call<SearchUser>, t: Throwable) {
                _errorMessage.value = Event(t.message.toString())
            }
        })
    }

    private fun getThemeSetting(preference: SettingPreference){
        viewModelScope.launch(Dispatchers.IO){
            val preferences = userFavoriteRepository.getThemeSetting(preference)
            _isDarkModeActive.postValue(preferences[SettingPreference.THEME_KEY])
        }
    }

    fun saveThemeSetting(preference: SettingPreference, isDarkModeActive:Boolean){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                userFavoriteRepository.saveThemeSetting(preference, isDarkModeActive)
            }
            getThemeSetting(preference)
        }
    }

    init {
        getThemeSetting(settingPreference)
    }
    companion object {
        var isRvAllowedToDisplay = false
    }
}