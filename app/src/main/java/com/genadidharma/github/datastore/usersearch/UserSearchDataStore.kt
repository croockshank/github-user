package com.genadidharma.github.datastore.usersearch

import androidx.paging.PagingSource
import com.genadidharma.github.model.UserSearchItem
import com.genadidharma.github.model.UserSearchRemoteKey

interface UserSearchDataStore {
    suspend fun insertUsers(users: MutableList<UserSearchItem>?)

    suspend fun insertRemoteKey(remoteKey: UserSearchRemoteKey)

    suspend fun updateToFavorite(userId: Int)

    suspend fun updateToNotFavorite(userId: Int)

    suspend fun getUsers(keyword: String, page: Int, perPage: Int): MutableList<UserSearchItem>?

    fun getUsersFromDB(keyword: String): PagingSource<Int, UserSearchItem>

    suspend fun getUsersRemoteKey(): UserSearchRemoteKey

    suspend fun deleteUsers()

    suspend fun deleteRemoteKey()

    suspend fun addAll(username: String, users: MutableList<UserSearchItem>?)
}
