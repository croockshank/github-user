package com.genadidharma.github.ui.usersearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.genadidharma.github.R
import com.genadidharma.github.model.UserSearchItem
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.user_list_item.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UserSearchAdapter(private val listener: (UserSearchItem?) -> Unit) :
    PagingDataAdapter<UserSearchItem, UserSearchAdapterViewHolder>(USER_COMPARATOR) {
    override fun onBindViewHolder(holder: UserSearchAdapterViewHolder, position: Int) {
        holder.bindItem(getItem(position), listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchAdapterViewHolder =
        UserSearchAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        )

    companion object {
        private val USER_COMPARATOR = object : DiffUtil.ItemCallback<UserSearchItem>() {
            override fun areItemsTheSame(
                oldItem: UserSearchItem, newItem: UserSearchItem
            ): Boolean =
                oldItem.login == newItem.login

            override fun areContentsTheSame(
                oldItem: UserSearchItem, newItem: UserSearchItem
            ): Boolean =
                oldItem == newItem

        }
    }
}

@ExperimentalCoroutinesApi
class UserSearchAdapterViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView),
    LayoutContainer {
    override val containerView: View?
        get() = itemView

    fun bindItem(
        item: UserSearchItem?,
        listener: (UserSearchItem?) -> Unit
    ) {
        if (item != null) {
            Picasso.get()
                .load(item.avatarUrl)
                .placeholder(R.drawable.profile_image_placeholder)
                .into(iv_avatar)
            tv_name.text = item.login
            tv_type.text = item.type ?: "-"
            if (item.isFavorite) iv_favorite.visibility = View.VISIBLE else iv_favorite.visibility =
                View.INVISIBLE
            containerView?.setOnClickListener { listener(item) }
        }
    }
}