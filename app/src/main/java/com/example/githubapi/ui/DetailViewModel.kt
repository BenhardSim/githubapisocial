package com.example.githubapi.ui

import android.util.Log
import androidx.lifecycle.*
import com.example.githubapi.data.GithubRepository
import com.example.githubapi.data.remote.response.UserItem
import com.example.githubapi.data.remote.response.UserProfile
import com.example.githubapi.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val githubRepository: GithubRepository): ViewModel() {

    companion object {
        private const val TAG = "DetailUser"
        var QUERY = "ben"
    }

    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> = _userProfile

    private val _userFollowers = MutableLiveData<List<UserItem>>()
    val userFollowers : LiveData<List<UserItem>> = _userFollowers

    private val _userFollowing = MutableLiveData<List<UserItem>>()
    val userFollowing : LiveData<List<UserItem>> = _userFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    init{
        findProfile()
        findListFollowers()
        findListFollowing()
    }

    fun addFavorite(username : String, avatar : String, isFavorite : Boolean){
        viewModelScope.launch {
            Log.d("DetailViewModel", "heyoo")
            githubRepository.setUserFavorite(username,avatar,isFavorite)
        }
    }

    fun isFavorite(username: String): LiveData<Boolean> = liveData {
        val res = githubRepository.isFavoriteUser(username)
        emit(res)
    }



    private fun findProfile(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserProfile(QUERY)
        client.enqueue(object : Callback<UserProfile>{
            override fun onResponse(
                call: Call<UserProfile>,
                response: Response<UserProfile>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userProfile.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    private fun findListFollowers(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(QUERY)
        client.enqueue(object : Callback<List<UserItem>> {
            override fun onResponse(
                call: Call<List<UserItem>>,
                response: Response<List<UserItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollowers.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }


    private fun findListFollowing(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(QUERY)
        client.enqueue(object : Callback<List<UserItem>> {
            override fun onResponse(
                call: Call<List<UserItem>>,
                response: Response<List<UserItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollowing.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

}