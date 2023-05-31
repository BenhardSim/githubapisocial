package com.example.githubapi.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapi.R

class UserAdapter(private val listUser: List<MutableMap<String,String>>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(username: MutableMap<String,String>)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val uri = listUser[position]["avatarurl"]
        val username = listUser[position]["username"]
        Glide.with(viewHolder.itemView.context).load(uri).circleCrop().into(viewHolder.avatarItem)
        viewHolder.usernameItem.text = username

        viewHolder.itemView.setOnClickListener{
            if (username != null) {
                onItemClickCallback.onItemClicked(listUser[position])
            }
        }

    }
    override fun getItemCount() = listUser.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatarItem : ImageView = view.findViewById(R.id.img_item_photo)
        val usernameItem : TextView = view.findViewById(R.id.tv_item_name)
    }


}