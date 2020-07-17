package com.genadidharma.github.datastore.usersearch

import androidx.paging.PagingSource
import com.genadidharma.github.model.UserSearchItem
import com.genadidharma.github.model.UserSearchRemoteKey
import com.genadidharma.github.webservice.GithubAPIService

class UserSearchRemoteDataStore (private val githubAPIService: GithubAPIService): UserSearchDataStore{
    override suspend fun insertUsers(users: MutableList<UserSearchItem>?) {
        TODO("Not yet implemented")
    }

    override suspend fun insertRemoteKey(remoteKey: UserSearchRemoteKey) {
        TODO("Not yet implemented")
    }

    override suspend fun updateToFavorite(userId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFromFavorite() {
        TODO("Not yet implemented")
    }

    override suspend fun getUsers(
        keyword: String,
        page: Int,
        perPage: Int
    ): MutableList<UserSearchItem>? {
        val response = githubAPIService.getUsers(keyword, page, perPage)
        if (response.isSuccessful){
            return response.body()?.items
        }

        throw Exception("Error: ${response.message()}(${response.code()})")
    }

    override fun getUsersFromDB(keyword: String): PagingSource<Int, UserSearchItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getUsersRemoteKey(): UserSearchRemoteKey {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUsers() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRemoteKey() {
        TODO("Not yet implemented")
    }

    override suspend fun addAll(username: String, users: MutableList<UserSearchItem>?) {
        TODO("Not yet implemented")
    }
}