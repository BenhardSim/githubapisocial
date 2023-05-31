package com.example.githubapi.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapi.data.remote.response.UserItem
import com.example.githubapi.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)

        val detailViewModel = ViewModelProvider(
            requireActivity(), ViewModelProvider.NewInstanceFactory()
        )[DetailViewModel::class.java]
        if (index == 1) {
            detailViewModel.userFollowers.observe(viewLifecycleOwner) { users ->
                setUserFollow(users)
            }

        } else if (index == 2) {
            detailViewModel.userFollowing.observe(viewLifecycleOwner) { users ->
                setUserFollow(users)
            }
        }


        detailViewModel.isLoading.observe(viewLifecycleOwner) {
            if(it){
                binding.empty.visibility = View.GONE
            }
            showLoading(it)
        }
        val layoutManager = LinearLayoutManager(activity)
        binding.followList.layoutManager = layoutManager


    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val USERNAME = "username"
    }

    private fun setUserFollow(FollowerList: List<UserItem>) {
        if (FollowerList.isEmpty()) {
            binding.empty.visibility = View.VISIBLE
            binding.followList.visibility = View.GONE
            binding.followList.adapter = null

        } else {
            binding.empty.visibility = View.GONE
            binding.followList.visibility = View.VISIBLE
            val listItem = ArrayList<MutableMap<String, String>>()
            for (user in FollowerList) {
                val item: MutableMap<String, String> = mutableMapOf()
                item["username"] = user.login
                item["avatarurl"] = user.avatarUrl
                listItem.add(item)
            }

            val adapter = UserAdapter(listItem)
            binding.followList.adapter = adapter

            adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(username: MutableMap<String, String>) {
                    showSelectedUsers(username)
                }
            })
        }

    }

    private fun showSelectedUsers(user: MutableMap<String, String>) {
        val moveIntentUsers = Intent(activity, DetailUser::class.java)
        moveIntentUsers.putExtra(DetailUser.USERNAME, user["username"])
        moveIntentUsers.putExtra(DetailUser.AVATAR, user["avatarurl"])
        startActivity(moveIntentUsers)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar2.visibility = View.VISIBLE
        } else {
            binding.progressBar2.visibility = View.GONE
        }
    }
}