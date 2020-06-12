package com.genadidharma.github.datastore.usersearch

import com.genadidharma.github.model.UserSearchItem

interface UserSearchSetDataStore {
    suspend fun getUsers(username: String): MutableList<UserSearchItem>?
    suspend fun addAll(username: String, users: MutableList<UserSearchItem>?)
}
