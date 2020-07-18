package com.genadidharma.githubconsumer.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.genadidharma.githubconsumer.R
import com.genadidharma.githubconsumer.model.UserFavoriteItem
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.user_list_item.*

class UserFavoriteAdapter() :
    RecyclerView.Adapter<UserFavoriteAdapterViewHolder>() {

    private val favoriteList = mutableListOf<UserFavoriteItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserFavoriteAdapterViewHolder =
        UserFavoriteAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.user_list_item,
                parent,
                false
            )
        )


    override fun getItemCount(): Int = favoriteList.size

    override fun onBindViewHolder(holder: UserFavoriteAdapterViewHolder, position: Int) {
        holder.bindItem(favoriteList[position])
    }

    fun updateData(newFavoriteList: MutableList<UserFavoriteItem>?) {
        newFavoriteList?.let {
            favoriteList.clear()
            favoriteList.addAll(it)
            notifyDataSetChanged()
        }
    }

}

class UserFavoriteAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    LayoutContainer {
    override val containerView: View?
        get() = itemView

    fun bindItem(item: UserFavoriteItem) {
        Picasso.get()
            .load(item.avatarUrl)
            .placeholder(R.drawable.profile_image_placeholder)
            .into(iv_avatar)
        tv_name.text = item.login
        tv_type.text = item.type ?: "-"
    }
}
