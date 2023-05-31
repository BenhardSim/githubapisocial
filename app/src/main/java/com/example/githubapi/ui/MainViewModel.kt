package com.example.githubapi.ui

import android.util.Log
import androidx.lifecycle.*
import com.example.githubapi.data.remote.response.UserItem
import com.example.githubapi.data.remote.response.Users
import com.example.githubapi.data.remote.retrofit.ApiConfig
import com.example.githubapi.utils.SettingPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    companion object {
        private const val TAG = "MainActivity"
        private const val QUERY = "ben"
    }

    private val _listUser = MutableLiveData<List<UserItem>>()
    val listUser: LiveData<List<UserItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        findUsers(QUERY)
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun findUsers(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(query)
        client.enqueue(object : Callback<Users> {
            override fun onResponse(
                call: Call<Users>,
                response: Response<Users>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }


}