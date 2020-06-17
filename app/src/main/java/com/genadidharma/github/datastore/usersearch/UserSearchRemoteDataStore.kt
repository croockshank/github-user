package com.genadidharma.github.datastore.usersearch

import com.genadidharma.github.model.UserSearchItem
import com.genadidharma.github.webservice.GithubAPIService
import java.lang.Exception

class UserSearchRemoteDataStore (private val githubAPIService: GithubAPIService): UserSearchDataStore{
    override suspend fun getUsers(username: String): MutableList<UserSearchItem>? {
        val response = githubAPIService.getUsers(username)
        if(response.isSuccessful){
            return response.body()?.items
        }

        throw Exception("${response.code()}: ${response.message()}")
    }

    override suspend fun addAll(username: String, users: MutableList<UserSearchItem>?) {
        TODO("Not yet implemented")
    }

}