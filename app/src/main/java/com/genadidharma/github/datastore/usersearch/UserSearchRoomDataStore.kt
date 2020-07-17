package com.genadidharma.github.datastore.usersearch

import androidx.paging.PagingSource
import com.genadidharma.github.model.UserSearchItem
import com.genadidharma.github.model.UserSearchRemoteKey
import com.genadidharma.github.db.UserSearchDao
import com.genadidharma.github.db.UserSearchRemoteKeyDao


class UserSearchRoomDataStore(private val userSearchDao: UserSearchDao, private val userSearchRemoteKeyDao: UserSearchRemoteKeyDao) :
    UserSearchDataStore {
    override suspend fun insertUsers(users: MutableList<UserSearchItem>?) {
        return userSearchDao.insertUsers(users)
    }

    override suspend fun insertRemoteKey(remoteKey: UserSearchRemoteKey) {
        return userSearchRemoteKeyDao.insertRemoteKey(remoteKey)
    }

    override suspend fun updateToFavorite(userId: Int) {
        return userSearchDao.updateToFavorite(userId = userId)
    }

    override suspend fun updateToNotFavorite(userId: Int) {
        return userSearchDao.updateToNotFavorite(userId = userId)
    }

    override suspend fun getUsers(
        keyword: String,
        page: Int,
        perPage: Int
    ): MutableList<UserSearchItem>? {
        TODO("Not yet implemented")
    }

    override fun getUsersFromDB(keyword: String): PagingSource<Int, UserSearchItem> {
        return userSearchDao.getUsers(keyword = "%$keyword%")
    }

    override suspend fun getUsersRemoteKey(): UserSearchRemoteKey {
        return userSearchRemoteKeyDao.getRemoteKey()
    }

    override suspend fun deleteUsers() {
        userSearchDao.deleteUsers()
    }

    override suspend fun deleteRemoteKey() {
        userSearchRemoteKeyDao.deleteRemoteKey()
    }

    override suspend fun addAll(username: String, users: MutableList<UserSearchItem>?) {
        TODO("Not yet implemented")
    }
}