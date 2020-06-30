package com.genadidharma.github.datastore.userfollowings

import com.genadidharma.github.datastore.userfollowers.UserFollowingsDataSource
import com.genadidharma.github.model.UserFollowingItem
import com.genadidharma.github.webservice.GithubAPIService

class UserFollowingRemoteDataSource(private val githubAPIService: GithubAPIService) :
    UserFollowingsDataSource {
    override suspend fun getFollowings(username: String): MutableList<UserFollowingItem>? {
        val response = githubAPIService.getUserFollowings(username)
        if (response.isSuccessful) return response.body()

        throw Exception("Error: ${response.message()}(${response.code()})")
    }

    override suspend fun addAll(username: String, followings: MutableList<UserFollowingItem>?) {
        TODO("Not yet implemented")
    }
}
