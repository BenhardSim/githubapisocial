package com.example.githubapi.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapi.R
import com.example.githubapi.data.remote.response.UserItem
import com.example.githubapi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>(){
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // observe live data
        mainViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        mainViewModel.listUser.observe(this) { user ->
            if (user.isEmpty()) {
                binding.rvUser.visibility = View.GONE
                binding.empty.visibility = View.VISIBLE
            } else {
                binding.rvUser.visibility = View.VISIBLE
                binding.empty.visibility = View.GONE
                setUserData(user)
            }
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        mainViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                val search = menu?.findItem(R.id.search)
                search?.setIcon(R.drawable.ic_baseline_search_24)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                val search = menu?.findItem(R.id.search)
                search?.setIcon(R.drawable.ic_baseline_search_24_black)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.rvUser.adapter = null
                mainViewModel.findUsers(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                val moveIntentUsers = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(moveIntentUsers)
                true
            }
            R.id.settings -> {
                val moveIntentSettings = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(moveIntentSettings)
                true
            }
            else -> true
        }
    }

    private fun showSelectedUsers(user: MutableMap<String, String>) {
        val moveIntentUsers = Intent(this@MainActivity, DetailUser::class.java)
        moveIntentUsers.putExtra(DetailUser.USERNAME, user["username"])
        moveIntentUsers.putExtra(DetailUser.AVATAR, user["avatarurl"])
        startActivity(moveIntentUsers)
    }

    private fun setUserData(userList: List<UserItem>) {
        val listItem = ArrayList<MutableMap<String, String>>()
        for (user in userList) {
            val item: MutableMap<String, String> = mutableMapOf()
            item["username"] = user.login
            item["avatarurl"] = user.avatarUrl
            listItem.add(item)
        }
        val adapter = UserAdapter(listItem)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(username: MutableMap<String, String>) {
                showSelectedUsers(username)
            }
        })


    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}