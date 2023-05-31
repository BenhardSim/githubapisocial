package com.example.githubapi.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.githubapi.data.remote.retrofit.APIService
import com.example.githubapi.data.local.entity.UserEntity
import com.example.githubapi.data.local.room.UserDao
import com.example.githubapi.utils.AppExecutors

class  GithubRepository(
    private val apiService: APIService,
    private val userDao: UserDao,
    private val appExecutors: AppExecutors
    ) {

    suspend fun setUserFavorite(username : String, avatar : String,  FavoriteState : Boolean){
        val user = UserEntity(username,avatar,FavoriteState)
        val userList: List <UserEntity> = listOf(user)
        if(FavoriteState){
            userDao.insertUser(userList)
        }else{
            userDao.deleteFavorite(username)
        }
    }

    fun getFavoriteUser() : LiveData<Result<List<UserEntity>>> = liveData {
        emit(Result.Loading)
        val localData: LiveData<Result<List<UserEntity>>> = userDao.getUser().map { Result.Success(it) }
        emitSource(localData)
    }

    suspend fun isFavoriteUser(username: String) : Boolean{
        return userDao.isUserFavorite(username)
    }

    companion object {
        @Volatile
        private var instance: GithubRepository? = null
        fun getInstance(
            apiService: APIService,
            userDao: UserDao,
            appExecutors: AppExecutors
        ): GithubRepository =
            instance ?: synchronized(this) {
                instance ?: GithubRepository(apiService, userDao, appExecutors)
            }.also { instance = it }
    }

}