package com.lukman.githubuser

import com.google.gson.annotations.SerializedName


data class SearchUser(
    @field:SerializedName("items")
    val items: ArrayList<ItemSearchUser>
)

data class ItemSearchUser(
    @field:SerializedName("login")
    val login: String,
    @field:SerializedName("avatar_url")
    val avatar_url: String,
    @field:SerializedName("html_url")
    val html_url: String
)

data class UserDetail(
    @field:SerializedName("id")
    val id:Long?,
    @field:SerializedName("login")
    val login: String,
    @field:SerializedName("avatar_url")
    val avatar_url: String,
    @field:SerializedName("html_url")
    val html_url: String,
    @field:SerializedName("name")
    var name: String?,
    @field:SerializedName("company")
    var company: String?,
    @field:SerializedName("location")
    var location: String?,
    @field:SerializedName("public_repos")
    var public_repos: Int?,
    @field:SerializedName("followers")
    val followers: Int,
    @field:SerializedName("following")
    val following: Int
)

data class UserRepo(
    @field:SerializedName("name")
    val name:String,
    @field:SerializedName("html_url")
    val html_url: String
)