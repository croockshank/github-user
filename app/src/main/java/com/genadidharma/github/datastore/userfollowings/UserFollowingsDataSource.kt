package com.genadidharma.github.datastore.userfollowers

import com.genadidharma.github.model.UserFollowingItem

interface UserFollowingsDataSource {
    suspend fun getFollowings(username: String): MutableList<UserFollowingItem>?
    suspend fun addAll(username: String, followings: MutableList<UserFollowingItem>?)
}