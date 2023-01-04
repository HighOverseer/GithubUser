package com.lukman.githubuser.data.remote.ApiService

import com.lukman.githubuser.BuildConfig
import com.lukman.githubuser.data.remote.ModelnResponse.Followering
import com.lukman.githubuser.SearchUser
import com.lukman.githubuser.UserDetail
import com.lukman.githubuser.UserRepo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Authorization:token ${BuildConfig.API_TOKEN}")
    @GET("search/users")
    fun getAllUsers(@Query("q") q: String): Call<SearchUser>

    @Headers("Authorization:token ${BuildConfig.API_TOKEN}")
    @GET("users/{username}")
    fun getUserDetail(@Path("username") username: String): Call<UserDetail>

    @Headers("Authorization:token ${BuildConfig.API_TOKEN}")
    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<ArrayList<Followering>>

    @Headers("Authorization:token ${BuildConfig.API_TOKEN}")
    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<ArrayList<Followering>>

    @Headers("Authorization:token ${BuildConfig.API_TOKEN}")
    @GET("users/{username}/repos")
    fun getUserRepos(@Path("username") username: String):Call<ArrayList<UserRepo>>
}