package com.genadidharma.github.repository

import com.genadidharma.github.datastore.usersearch.UserSearchSetDataStore
import com.genadidharma.github.model.UserSearchItem

class UserSearchRepository private constructor(): BaseRepository<UserSearchSetDataStore>(){

    suspend fun getUsers(username: String): MutableList<UserSearchItem>?{
        return remoteDataStore?.getUsers(username)
    }

    companion object{
        val instance by lazy { UserSearchRepository() }
    }
}