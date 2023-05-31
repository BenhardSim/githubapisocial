package com.example.githubapi.DI

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.githubapi.data.GithubRepository
import com.example.githubapi.data.local.room.UserDatabase
import com.example.githubapi.data.remote.retrofit.ApiConfig
import com.example.githubapi.utils.AppExecutors
import com.example.githubapi.utils.SettingPreferences


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object Injection {
    fun provideRepository(context: Context): GithubRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        val appExecutors = AppExecutors()
        return GithubRepository.getInstance(apiService, dao, appExecutors)
    }

    fun providePreferences(context: Context) : SettingPreferences {
        return SettingPreferences.getInstance(context.dataStore)
    }
}