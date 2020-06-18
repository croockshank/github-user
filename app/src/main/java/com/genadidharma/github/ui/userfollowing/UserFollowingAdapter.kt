package com.genadidharma.github.ui.userfollowing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.genadidharma.github.R
import com.genadidharma.github.model.UserFollowingItem
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.user_list_item.*

class UserFollowingAdapter :
    RecyclerView.Adapter<UserFollowingAdapter.UserFollowersAdapterViewHolder>() {
    private val userFollowingsList = mutableListOf<UserFollowingItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFollowersAdapterViewHolder =
        UserFollowersAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        )

    override fun getItemCount(): Int = userFollowingsList.size

    override fun onBindViewHolder(holder: UserFollowersAdapterViewHolder, position: Int) {
        holder.bindItem(userFollowingsList[position])
    }

    fun updateData(newUserSearchList: MutableList<UserFollowingItem>) {
        userFollowingsList.clear()
        userFollowingsList.addAll(newUserSearchList)
        notifyDataSetChanged()
    }

    class UserFollowersAdapterViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        LayoutContainer {
        override val containerView: View?
            get() = itemView

        fun bindItem(item: UserFollowingItem) {
            Picasso.get()
                .load(item.avatarUrl)
                .into(iv_avatar)
            tv_name.text = item.login ?: "-"
            tv_type.text = item.type ?: "-"
        }
    }
}