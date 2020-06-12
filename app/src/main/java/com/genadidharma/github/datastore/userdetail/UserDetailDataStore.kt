package com.genadidharma.github.datastore.userdetail

import com.genadidharma.github.model.UserDetailItem

interface UserDetailDataStore {
    suspend fun getUser(username: String): UserDetailItem?
    suspend fun addAll(username: String, user: UserDetailItem?)
}