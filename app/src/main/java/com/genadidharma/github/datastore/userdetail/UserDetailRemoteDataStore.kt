package com.genadidharma.github.datastore.userdetail

import com.genadidharma.github.model.UserDetailItem
import com.genadidharma.github.webservice.GithubAPIService

class UserDetailRemoteDataStore (private val githubAPIService: GithubAPIService): UserDetailDataStore {
    override suspend fun getUser(username: String): UserDetailItem? {
        val response = githubAPIService.getUserDetail(username)
        if(response.isSuccessful) return response.body()

        throw Exception("Error: ${response.message()}(${response.code()})")
    }

    override suspend fun addAll(username: String, user: UserDetailItem?) {
        TODO("Not yet implemented")
    }
}