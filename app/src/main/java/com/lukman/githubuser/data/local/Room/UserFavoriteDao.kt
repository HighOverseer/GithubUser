package com.lukman.githubuser.data.local.Room

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lukman.githubuser.data.local.Entity.UserFavorite

@androidx.room.Dao
interface UserFavoriteDao {
    @Query("SELECT * FROM Favorite_Users")
    fun getUserFavorites():LiveData<List<UserFavorite>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun tambahUserKeFavorite(userFavorite: UserFavorite)

    @Delete
    suspend fun hapusDariFavorite(userFavorite: UserFavorite)

    @Query("SELECT EXISTS(SELECT * FROM Favorite_Users WHERE id = :id)")
    suspend fun IsUserFavorite(id:Long):Boolean
}