package com.example.githubapi.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubapi.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getUser(): LiveData<List<UserEntity>>

    @Query("DELETE FROM users WHERE username = :username")
    suspend fun deleteFavorite(username: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(news: List<UserEntity>)

    @Query("SELECT EXISTS(SELECT * FROM users WHERE username = :username AND favorite = 1)")
    suspend fun isUserFavorite(username: String): Boolean
}