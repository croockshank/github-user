package com.genadidharma.github.datastore.userfollowers

import com.genadidharma.github.model.UserFollowingsItem
import com.genadidharma.github.webservice.GithubAPIService

class UserFollowingsRemoteDataSource(private val githubAPIService: GithubAPIService) :
    UserFollowingsDataSource {
    override suspend fun getFollowings(username: String): MutableList<UserFollowingsItem>? {
        val response = githubAPIService.getUserFollowings(username);
        if (response.isSuccessful) return response.body()

        throw Exception("Error: ${response.message()}(${response.code()})")
    }

    override suspend fun addAll(username: String, followings: MutableList<UserFollowingsItem>?) {
        TODO("Not yet implemented")
    }
}
