package com.genadidharma.github.datastore.userfollowers

import com.genadidharma.github.model.UserFollowersItem
import com.genadidharma.github.model.UserFollowingsItem

interface UserFollowingsDataSource {
    suspend fun getFollowings(username: String): MutableList<UserFollowingsItem>?
    suspend fun addAll(username: String, followings: MutableList<UserFollowingsItem>?)
}