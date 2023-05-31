package com.example.githubapi.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class UserEntity (
    @field:ColumnInfo(name = "username")
    @field:PrimaryKey
    val username: String,

    @field:ColumnInfo(name = "AvatarUrl")
    val AvatarUrl: String,

    @field:ColumnInfo(name = "favorite")
    var isFavorite: Boolean,
)