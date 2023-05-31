package com.example.githubapi.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubapi.DI.Injection
import com.example.githubapi.data.GithubRepository
import com.example.githubapi.utils.SettingPreferences

class ViewModelFactory private constructor(private val githubRepository: GithubRepository, private val preference : SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(githubRepository) as T
        }else if(modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
            return  FavoriteViewModel(githubRepository) as T
        }else if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return  MainViewModel(preference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), Injection.providePreferences((context)))
            }.also { instance = it }
    }
}