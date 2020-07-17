package com.genadidharma.github.ui.usersearch

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.genadidharma.github.R
import com.genadidharma.github.model.UserSearchItem
import com.genadidharma.github.ui.userdetail.UserDetailActivity
import com.genadidharma.github.util.CustomOnItemClickListener
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.user_list_item.*

class UserSearchAdapter :
    PagingDataAdapter<UserSearchItem, UserSearchAdapterViewHolder>(USER_COMPARATOR) {
    override fun onBindViewHolder(holder: UserSearchAdapterViewHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchAdapterViewHolder =
        UserSearchAdapterViewHolder(
            parent.context,
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

class UserSearchAdapterViewHolder(private val context: Context, itemView: View) :
    RecyclerView.ViewHolder(itemView),
    LayoutContainer {
    override val containerView: View?
        get() = itemView

    fun bindItem(item: UserSearchItem?) {
        Picasso.get()
            .load(item?.avatarUrl)
            .placeholder(R.drawable.profile_image_placeholder)
            .into(iv_avatar)
        tv_name.text = item?.login ?: "-"
        tv_type.text = item?.type ?: "-"

        containerView?.setOnClickListener(
            CustomOnItemClickListener(
                adapterPosition,
                object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(context, UserDetailActivity::class.java)
/*                        intent.putExtra(
                            MainActivity.USER_FAVORITE_ITEM_TAG,
                            item.toUserFavoriteItem()
                        )
                        intent.putExtra(MainActivity.USER_POSITION_TAG, position)
                        activity.startActivityForResult(
                            intent,
                            MainActivity.REQUEST_LIKE
                        )*/
                        context.startActivity(intent)
                    }
                }
            )
        )
    }
}