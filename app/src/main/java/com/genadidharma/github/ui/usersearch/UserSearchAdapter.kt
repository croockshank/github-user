package com.genadidharma.github.ui.usersearch

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.genadidharma.github.R
import com.genadidharma.github.model.UserFavoriteItem
import com.genadidharma.github.model.UserSearchItem
import com.genadidharma.github.model.toUserFavoriteItem
import com.genadidharma.github.ui.userdetail.UserDetailActivity
import com.genadidharma.github.util.CustomOnItemClickListener
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.user_list_item.*

class UserSearchAdapter(private val activity: Activity) : RecyclerView.Adapter<UserSearchAdapter.UserSearchAdapterViewHolder>() {
    private val userSearchList = mutableListOf<UserSearchItem>()
    private val userSearchListFavorite = mutableListOf<UserFavoriteItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchAdapterViewHolder =
        UserSearchAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        )

    override fun getItemCount(): Int = userSearchList.size

    override fun onBindViewHolder(holder: UserSearchAdapterViewHolder, position: Int) {
        setFavoriteIfTrue()
        holder.bindItem(userSearchList[position])
    }

    fun updateData(newUserSearchList: MutableList<UserSearchItem>) {
        userSearchList.clear()
        userSearchList.addAll(newUserSearchList)
        notifyDataSetChanged()
    }

    fun updateFavoriteData(newUserFavoriteList: MutableList<UserFavoriteItem>){
        userSearchListFavorite.clear()
        userSearchListFavorite.addAll(newUserFavoriteList)
        notifyDataSetChanged()
    }

    private fun setFavoriteIfTrue(){
        userSearchList.map {userSearchItem ->
            userSearchListFavorite.map {userFavoriteItem ->
                if(userSearchItem.login.equals(userFavoriteItem.login))
                    userSearchItem.isFavorite = true
            }
        }
    }

    fun updateItem(position: Int, isFavorite: Boolean){
        this.userSearchList[position].isFavorite = isFavorite
        notifyItemChanged(position)
    }

    inner class UserSearchAdapterViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        LayoutContainer {
        override val containerView: View?
            get() = itemView

        fun bindItem(item: UserSearchItem) {
            Picasso.get()
                .load(item.avatarUrl)
                .into(iv_avatar)
            tv_name.text = item.login ?: "-"
            tv_type.text = item.type ?: "-"

            containerView?.setOnClickListener (
                CustomOnItemClickListener(
                    adapterPosition,
                    object : CustomOnItemClickListener.OnItemClickCallback{
                        override fun onItemClicked(view: View, position: Int) {
                            val intent = Intent(activity, UserDetailActivity::class.java)
                            intent.putExtra(MainActivity.USER_FAVORITE_ITEM_TAG, item.toUserFavoriteItem())
                            intent.putExtra(MainActivity.USER_POSITION_TAG, position)
                            activity.startActivityForResult(
                                intent,
                                MainActivity.REQUEST_LIKE
                            )
                        }

                    }
                )
            )
        }
    }
}