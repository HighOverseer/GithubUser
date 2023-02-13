package com.lukman.githubuser.ViewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukman.githubuser.data.remote.ApiClient.API
import com.lukman.githubuser.data.remote.ModelnResponse.Followering
import com.lukman.githubuser.SingleEvent.Event
import com.lukman.githubuser.UserDetail
import com.lukman.githubuser.UserRepo
import com.lukman.githubuser.data.UserFavoriteRepository
import com.lukman.githubuser.data.local.Entity.UserFavorite
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val userFavoriteRepository = UserFavoriteRepository(application)

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userDetail = MutableLiveData<UserDetail>()
    val userDetail: LiveData<UserDetail> = _userDetail

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    private val _followingList = MutableLiveData<ArrayList<Followering>>()
    val followingList: LiveData<ArrayList<Followering>> = _followingList

    private val _followerList = MutableLiveData<ArrayList<Followering>>()
    val followerList: LiveData<ArrayList<Followering>> = _followerList

    private val _repoList = MutableLiveData<ArrayList<UserRepo>>()
    val repoList:LiveData<ArrayList<UserRepo>> = _repoList

    private val _isThisUserFavorite = MutableLiveData<Boolean>(false)
    val isThisUserFavorite:LiveData<Boolean> = _isThisUserFavorite

    fun getDetailUser(username: String) {
        val requestUserDetail = API.getApiService().getUserDetail(username)
        val requestUserFollowing = API.getApiService().getFollowing(username)
        val requestUserFollower = API.getApiService().getFollowers(username)
        val requestUserRepo = API.getApiService().getUserRepos(username)
        requestUserDetail.enqueue(object : Callback<UserDetail> {
            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    checkNull(response.body()!!)
                    _userDetail.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                _errorMessage.postValue(Event(t.message.toString()))
            }
        })
        requestUserFollowing.enqueue(object : Callback<ArrayList<Followering>> {
            override fun onResponse(
                call: Call<ArrayList<Followering>>,
                response: Response<ArrayList<Followering>>
            ) {
                if (response.isSuccessful) {
                    _followingList.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ArrayList<Followering>>, t: Throwable) {
                _errorMessage.postValue(Event(t.message.toString()))
            }
        })
        requestUserFollower.enqueue(object : Callback<ArrayList<Followering>> {
            override fun onResponse(
                call: Call<ArrayList<Followering>>,
                response: Response<ArrayList<Followering>>
            ) {
                if (response.isSuccessful) {
                    _followerList.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ArrayList<Followering>>, t: Throwable) {
                _errorMessage.postValue(Event(t.message.toString()))
            }
        })

        requestUserRepo.enqueue(object : Callback<ArrayList<UserRepo>> {
            override fun onResponse(
                call: Call<ArrayList<UserRepo>>,
                response: Response<ArrayList<UserRepo>>
            ) {
                if (response.isSuccessful) {
                    _repoList.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ArrayList<UserRepo>>, t: Throwable) {
                _errorMessage.postValue(Event(t.message.toString()))
            }
        })

    }

    fun addDeleteThisUserFavorite(userDetail: UserDetail, isUserFavoriteFromLayout:Boolean){
        viewModelScope.launch {
            val (id, username, avatar_url, html_url, _, _, _, _, _, _) = userDetail
            val userFavorite = UserFavorite(id!!.toLong(), username, avatar_url, html_url)

            val wasFavoriteFromDB = userFavoriteRepository.isUserFavorite(userDetail.id!!)
            if(wasFavoriteFromDB != isUserFavoriteFromLayout){
                if (isUserFavoriteFromLayout){
                    addThisUserToFavorite(userFavorite)
                }else {
                    deleteThisUserFromFavorite(userFavorite)
                }
            }
        }

    }

    private suspend fun addThisUserToFavorite(userFavorite: UserFavorite){
        userFavoriteRepository.tambahUserKeFavorite(userFavorite)
    }

    private suspend fun deleteThisUserFromFavorite(userFavorite: UserFavorite){
        userFavoriteRepository.hapusUserDariFavorite(userFavorite)

    }

    private fun checkNull(userDetail: UserDetail) {
        userDetail.apply {
            if (name == null) {
                name = NOT_AVAILABLE
            }
            if (company == null) {
                company = NOT_AVAILABLE
            }
            if (location == null) {
                location = NOT_AVAILABLE
            }
            if (public_repos == null) {
                public_repos = 0
            }
        }
    }

    fun checkUserFavoriteStatus(id: Long?) {
        viewModelScope.launch {
            if(id!=null){
                _isThisUserFavorite.value = userFavoriteRepository.isUserFavorite(id)
            }

        }
    }

    fun setUserFavoriteStatus(isFavorite:Boolean){
        _isThisUserFavorite.value = isFavorite
    }

    companion object{
        const val NOT_AVAILABLE = "Tidak Tersedia"
    }
}
