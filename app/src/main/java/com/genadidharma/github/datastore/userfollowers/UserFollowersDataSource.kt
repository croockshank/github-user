package com.genadidharma.github.datastore.userfollowers

import com.genadidharma.github.model.UserFollowersItem

interface UserFollowersDataSource {
    suspend fun getFollowers(username: String): MutableList<UserFollowersItem>?
    suspend fun addAll(username: String, followers: MutableList<UserFollowersItem>?)
}