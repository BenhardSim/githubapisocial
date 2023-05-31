package com.example.githubapi.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubapi.R
import com.example.githubapi.ui.DetailViewModel.Companion.QUERY
import com.example.githubapi.data.remote.response.UserProfile
import com.example.githubapi.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailUser : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailUserBinding
    private val detailViewModel by viewModels<DetailViewModel>(){
        ViewModelFactory.getInstance(application)
    }
    companion object {
        const val USERNAME = "user_name"
        const val AVATAR = "avatar_url"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
    private lateinit var username : String
    private lateinit var avatar : String

    var _isFavorite = MutableLiveData<Boolean>(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val bundle = intent.extras

        if (bundle != null) {
            username = bundle.getString(USERNAME).toString()
            avatar = bundle.getString(AVATAR).toString()
            QUERY = username
            detailViewModel.isFavorite(username).observe(this) { result ->
                fillStar(result)
                _isFavorite.value = result != true
            }

        }

        detailViewModel.userProfile.observe(this) { profile -> setUserProfile(profile) }
        detailViewModel.isLoading.observe(this) { showLoading(it) }

        binding.favoriteBtn.setOnClickListener(this)

        val sectionsPagerAdapter = SectionsPagerAdapter(this,username)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs : TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        val search = menu.findItem(R.id.search)
        search.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                val moveIntentUsers = Intent(this@DetailUser, FavoriteActivity::class.java)
                startActivity(moveIntentUsers)
                true
            }
            R.id.settings -> {
                val moveIntentSettings = Intent(this@DetailUser, SettingsActivity::class.java)
                startActivity(moveIntentSettings)
                true
            }
            else -> true
        }
    }

    private fun fillStar(filled : Boolean){
        var mark = binding.favoriteBtn
        if(filled){
            mark.setImageDrawable(ContextCompat.getDrawable(mark.context, R.drawable.ic_baseline_star_24))
        }else{
            mark.setImageDrawable(ContextCompat.getDrawable(mark.context, R.drawable.ic_baseline_star_border_24))
        }
    }

    private fun setUserProfile(Profiles : UserProfile){
        binding.username.text = Profiles.login
        val uri = Profiles.avatarUrl
        Glide.with(this).load(uri).circleCrop().into(binding.profilepic)
        val followers : Int = Profiles.followers
        val following : Int = Profiles.following
        binding.followers.text = getString(R.string.followersCnt, followers)
        binding.following.text = getString(R.string.followingCnt, following)
        binding.name.text = Profiles.name

    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.favorite_btn -> {
                detailViewModel.isFavorite(username).observe(this) { result ->
                    _isFavorite.value = result != true
                    fillStar(!result)
                    Log.d("DetailUser", _isFavorite.value.toString())
                    _isFavorite.value?.let { detailViewModel.addFavorite(username,avatar, it) }
                }

            }
        }
    }


}