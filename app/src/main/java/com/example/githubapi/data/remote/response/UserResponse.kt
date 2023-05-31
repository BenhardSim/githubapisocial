package com.example.githubapi.data.remote.response

import com.google.gson.annotations.SerializedName

data class Users(
	@field:SerializedName("items")
	val items: List<UserItem>,
)

data class UserItem(

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("id")
	val id: Int,


)

data class UserProfile(
	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("followers")
	val followers: Int,

	@field:SerializedName("following")
	val following: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("name")
	val name: String,

)



