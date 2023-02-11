package com.lukman.githubuser.data.remote.ApiService

import com.lukman.githubuser.data.remote.ModelnResponse.Followering
import com.lukman.githubuser.SearchUser
import com.lukman.githubuser.UserDetail
import com.lukman.githubuser.UserRepo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    fun getAllUsers(@Query("q") q: String): Call<SearchUser>


    @GET("users/{username}")
    fun getUserDetail(@Path("username") username: String): Call<UserDetail>


    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<ArrayList<Followering>>


    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<ArrayList<Followering>>


    @GET("users/{username}/repos")
    fun getUserRepos(@Path("username") username: String):Call<ArrayList<UserRepo>>
}