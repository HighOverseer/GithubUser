package com.lukman.githubuser.data.local.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorite_Users")
data class UserFavorite(
    @ColumnInfo("id")
    @PrimaryKey
    val id:Long,

    @field:ColumnInfo("login")
    val login: String,

    @field:ColumnInfo("avatar_url")
    val avatar_url: String,

    @field:ColumnInfo("html_url")
    val html_url:String?
)