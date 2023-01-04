package com.lukman.githubuser.Helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lukman.githubuser.ViewModel.DetailViewModel
import com.lukman.githubuser.ViewModel.FavoriteViewModel
import com.lukman.githubuser.ViewModel.MainViewModel
import com.lukman.githubuser.ViewModel.SplashViewModel

class ViewModelFactory private constructor(private val mApplication: Application):ViewModelProvider.NewInstanceFactory() {
    companion object{
        @Volatile
        private var instance:ViewModelFactory?=null

        fun getInstance(application: Application):ViewModelFactory{
            return instance?: synchronized(ViewModelFactory::class.java){
                instance = ViewModelFactory(application)
                instance as ViewModelFactory
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(mApplication) as T
        }else if(modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
            return FavoriteViewModel(mApplication) as T
        }else if(modelClass.isAssignableFrom(SplashViewModel::class.java)){
            return SplashViewModel(mApplication) as T
        }else if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class : ${modelClass.name}")
    }
}