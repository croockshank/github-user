package com.genadidharma.github.ui.usersearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.genadidharma.github.R
import com.genadidharma.github.model.UserSearchItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.user_list_item.*

class UserSearchAdapter : RecyclerView.Adapter<UserSearchAdapter.UserSearchAdapterViewHolder>() {
    private val userSearchList = mutableListOf<UserSearchItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchAdapterViewHolder =
        UserSearchAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        )

    override fun getItemCount(): Int = userSearchList.size

    override fun onBindViewHolder(holder: UserSearchAdapterViewHolder, position: Int) {
        holder.bindItem(userSearchList[position])
    }

    fun updateData(newUserSearchList: MutableList<UserSearchItem>){
        userSearchList.clear()
        userSearchList.addAll(newUserSearchList)
        notifyDataSetChanged()
    }

    class UserSearchAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        LayoutContainer {
        override val containerView: View?
            get() = itemView

        fun bindItem(item: UserSearchItem) {
            Glide.with(containerView!!)
                .load(item.avatarUrl)
                .into(iv_avatar)
            tv_name.text = item.login
        }
    }
}