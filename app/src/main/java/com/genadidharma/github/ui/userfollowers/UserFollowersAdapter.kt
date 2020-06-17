package com.genadidharma.github.ui.userfollowers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.genadidharma.github.R
import com.genadidharma.github.model.UserFollowersItem
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.user_list_item.*

class UserFollowersAdapter :
    RecyclerView.Adapter<UserFollowersAdapter.UserFollowersAdapterViewHolder>() {
    private val userFollowersList = mutableListOf<UserFollowersItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFollowersAdapterViewHolder =
        UserFollowersAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        )

    override fun getItemCount(): Int = userFollowersList.size

    override fun onBindViewHolder(holder: UserFollowersAdapterViewHolder, position: Int) {
        holder.bindItem(userFollowersList[position])
    }

    fun updateData(newUserSearchList: MutableList<UserFollowersItem>) {
        userFollowersList.clear()
        userFollowersList.addAll(newUserSearchList)
        notifyDataSetChanged()
    }

    class UserFollowersAdapterViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        LayoutContainer {
        override val containerView: View?
            get() = itemView

        fun bindItem(item: UserFollowersItem) {
            Picasso.get()
                .load(item.avatarUrl)
                .into(iv_avatar)
            tv_name.text = item.login ?: "-"
            tv_type.text = item.type ?: "-"
        }
    }
}