package com.example.githubapi.data.remote.retrofit

import com.example.githubapi.data.remote.response.UserItem
import com.example.githubapi.data.remote.response.UserProfile
import com.example.githubapi.data.remote.response.Users
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @GET("search/users")
    fun getUsers(
        @Query("q") q: String
    ): Call<Users>

    @GET("users/{username}")
    fun getUserProfile(
        @Path("username") username: String
    ): Call<UserProfile>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<UserItem>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<UserItem>>


}
