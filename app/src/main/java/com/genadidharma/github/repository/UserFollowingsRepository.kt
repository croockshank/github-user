package com.genadidharma.github.repository

import com.genadidharma.github.datastore.userfollowers.UserFollowingsDataSource
import com.genadidharma.github.model.UserFollowingsItem

class UserFollowingsRepository private constructor(): BaseRepository<UserFollowingsDataSource>(){
    suspend fun getFollowings(username: String): MutableList<UserFollowingsItem>?{
        return remoteDataStore?.getFollowings(username)
    }

    companion object{
        val instance by lazy { UserFollowingsRepository() }
    }
}