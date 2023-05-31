package com.example.githubapi.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapi.databinding.ActivityFavoriteBinding
import com.example.githubapi.data.Result
import com.example.githubapi.data.local.entity.UserEntity

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel>(){
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        setUserFavorite(user)
        favoriteViewModel.getFavoriteUser().observe(this){result ->
            if(result != null){
                when(result){
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE

                    }
                    is Result.Success -> {
                        if(result.data.isEmpty()){
                            binding.rvFavorite.visibility = View.GONE
                            binding.empty.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                        }else{
                            binding.rvFavorite.visibility = View.VISIBLE
                            binding.empty.visibility = View.GONE
                            binding.progressBar.visibility = View.GONE
                            val userData = result.data
                            setUserFavorite(userData)
                        }
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }

        }


        val layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.layoutManager = layoutManager
    }

    private fun setUserFavorite(userList: List<UserEntity>) {
        val listItem = ArrayList<MutableMap<String, String>>()
        val userListMap = userList.associateBy({it.username}, {it.AvatarUrl})
        userListMap.forEach{(username, AvatarUrl) ->
            val item: MutableMap<String, String> = mutableMapOf()
            item["username"] = username
            item["avatarurl"] = AvatarUrl
            listItem.add(item)
        }
//        for (user in userList) {
//            val item: MutableMap<String, String> = mutableMapOf()
//            item["username"] = user.login
//            item["avatarurl"] = user.avatarUrl
//            listItem.add(item)
//        }
        val adapter = UserAdapter(listItem)
        binding.rvFavorite.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(username: MutableMap<String, String>) {
                showSelectedUsers(username)
            }
        })
    }


    private fun showSelectedUsers(user: MutableMap<String, String>) {
        val moveIntentUsers = Intent(this@FavoriteActivity, DetailUser::class.java)
        moveIntentUsers.putExtra(DetailUser.USERNAME, user["username"])
        moveIntentUsers.putExtra(DetailUser.AVATAR, user["avatarurl"])
        startActivity(moveIntentUsers)
    }
}