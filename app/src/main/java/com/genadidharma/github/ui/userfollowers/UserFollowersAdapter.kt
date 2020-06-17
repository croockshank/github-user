package com.genadidharma.github.ui.usersearch

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.genadidharma.github.R
import com.genadidharma.github.model.UserSearchItem
import com.genadidharma.github.ui.userdetail.UserDetailActivity
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.user_list_item.*

class UserSearchAdapter : RecyclerView.Adapter<UserSearchAdapter.UserSearchAdapterViewHolder>() {
    private val userSearchList = mutableListOf<UserSearchItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchAdapterViewHolder =
        UserSearchAdapterViewHolder(
            parent.context,
            LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        )

    override fun getItemCount(): Int = userSearchList.size

    override fun onBindViewHolder(holder: UserSearchAdapterViewHolder, position: Int) {
        holder.bindItem(userSearchList[position])
    }

    fun updateData(newUserSearchList: MutableList<UserSearchItem>) {
        userSearchList.clear()
        userSearchList.addAll(newUserSearchList)
        notifyDataSetChanged()
    }

    class UserSearchAdapterViewHolder(private val context: Context, itemView: View) :
        RecyclerView.ViewHolder(itemView),
        LayoutContainer {
        override val containerView: View?
            get() = itemView

        fun bindItem(item: UserSearchItem) {
            Picasso.get()
                .load(item.avatarUrl)
                .into(iv_avatar)
            tv_name.text = item.login
            tv_type.text = item.type

            containerView?.setOnClickListener {
                val intent = Intent(context, UserDetailActivity::class.java)
                intent.putExtra(MainActivity.USERNAME_ITEM_TAG, item.login)
                context.startActivity(intent)
            }
        }
    }
}