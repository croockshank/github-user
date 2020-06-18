package com.genadidharma.github.repository

import com.genadidharma.github.datastore.userfollowers.UserFollowingsDataSource
import com.genadidharma.github.model.UserFollowingItem

class UserFollowingRepository private constructor(): BaseRepository<UserFollowingsDataSource>(){
    suspend fun getFollowings(username: String): MutableList<UserFollowingItem>?{
        return remoteDataStore?.getFollowings(username)
    }

    companion object{
        val instance by lazy { UserFollowingRepository() }
    }
}