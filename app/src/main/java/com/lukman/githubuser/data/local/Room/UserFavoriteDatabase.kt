package com.lukman.githubuser.data.local.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lukman.githubuser.data.local.Entity.UserFavorite

@Database(entities = [UserFavorite::class], version = 2, exportSchema = false)
abstract class UserFavoriteDatabase:RoomDatabase() {
    abstract fun uFavoriteDao():UserFavoriteDao

    companion object{
        @Volatile
        private var instance:UserFavoriteDatabase?=null
        private const val DB_NAME = "favorite_user.db"
        private var migration1To2: Migration = object: Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Favorite_Users ADD COLUMN html_url TEXT")
            }
        }
        fun getInstance(context: Context):UserFavoriteDatabase{
            return instance?: synchronized(UserFavoriteDatabase::class.java){
                instance =  Room.databaseBuilder(
                    context.applicationContext,
                    UserFavoriteDatabase::class.java,
                    DB_NAME
                ).addMigrations(migration1To2).build()
                instance as UserFavoriteDatabase
            }
        }
    }
}