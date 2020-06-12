package com.genadidharma.github.repository

import com.genadidharma.github.datastore.userfollowers.UserFollowersDataSource
import com.genadidharma.github.model.UserFollowersItem

class UserFollowersRepository private constructor(): BaseRepository<UserFollowersDataSource>(){
    suspend fun getFollowers(username: String): MutableList<UserFollowersItem>?{
        return remoteDataStore?.getFollowers(username)
    }

    companion object{
        val instance by lazy { UserFollowersRepository() }
    }
}