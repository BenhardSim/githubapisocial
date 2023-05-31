package com.example.githubapi.ui

import androidx.lifecycle.ViewModel
import com.example.githubapi.data.GithubRepository

class FavoriteViewModel(private val githubRepository: GithubRepository) : ViewModel()  {

    fun getFavoriteUser() = githubRepository.getFavoriteUser()

}