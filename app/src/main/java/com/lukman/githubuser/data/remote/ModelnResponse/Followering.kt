package com.lukman.githubuser.data.remote.ModelnResponse

import com.google.gson.annotations.SerializedName


data class Followering(
    @field:SerializedName("login")
    val login: String,
    @field:SerializedName("avatar_url")
    val avatar_url: String,
    @field:SerializedName("html_url")
    val html_url: String
)
