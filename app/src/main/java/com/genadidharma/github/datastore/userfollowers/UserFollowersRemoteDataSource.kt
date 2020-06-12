package com.genadidharma.github.datastore.userfollowers

import com.genadidharma.github.model.UserFollowersItem
import com.genadidharma.github.webservice.GithubAPIService

class UserFollowersRemoteDataSource(private val githubAPIService: GithubAPIService) :
    UserFollowersDataSource {
    override suspend fun getFollowers(username: String): MutableList<UserFollowersItem>? {
        val response = githubAPIService.getUserFollowers(username);
        if (response.isSuccessful) return response.body()?.items

        throw java.lang.Exception("${response.code()}: ${response.message()}")
    }

    override suspend fun addAll(username: String, followers: MutableList<UserFollowersItem>?) {
        TODO("Not yet implemented")
    }
}
