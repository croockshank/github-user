package com.genadidharma.github.datastore.userdetail

import com.genadidharma.github.model.UserDetailItem
import com.genadidharma.github.webservice.GithubAPIService
import java.lang.Exception

class UserDetailRemoteDataStore (private val githubAPIService: GithubAPIService): UserDetailDataStore {
    override suspend fun getUser(username: String): UserDetailItem? {
        val response = githubAPIService.getUserDetail(username)
        if(response.isSuccessful) return response.body()

        throw Exception("${response.code()}: ${response.message()}")
    }

    override suspend fun addAll(username: String, user: UserDetailItem?) {
        TODO("Not yet implemented")
    }
}