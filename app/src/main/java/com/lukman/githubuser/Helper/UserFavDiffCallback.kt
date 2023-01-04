package com.lukman.githubuser.Helper

import androidx.recyclerview.widget.DiffUtil
import com.lukman.githubuser.data.local.Entity.UserFavorite

class UserFavDiffCallback(private val mOldUserFavList:List<UserFavorite>, private val mNewOldUserFavList:List<UserFavorite>)
    :DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldUserFavList.size
    }

    override fun getNewListSize(): Int {
        return mNewOldUserFavList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldUserFavList[oldItemPosition].id ==  mNewOldUserFavList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUserFavList = mOldUserFavList[oldItemPosition]
        val newUserFavList = mNewOldUserFavList[newItemPosition]
        return oldUserFavList.login == newUserFavList.login && oldUserFavList.avatar_url == newUserFavList.avatar_url && newUserFavList.html_url == oldUserFavList.html_url
    }
}