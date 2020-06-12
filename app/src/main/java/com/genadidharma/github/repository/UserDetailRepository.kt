package com.genadidharma.github.repository

import com.genadidharma.github.datastore.userdetail.UserDetailDataStore
import com.genadidharma.github.model.UserDetailItem

class UserDetailRepository private constructor(): BaseRepository<UserDetailDataStore>(){
    suspend fun getUser(username: String): UserDetailItem?{
        return remoteDataStore?.getUser(username)
    }

    companion object{
        val instance by lazy { UserDetailRepository() }
    }
}