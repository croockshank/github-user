package com.genadidharma.github.datastore.userfollowers

import com.genadidharma.github.model.UserFollowersItem
import com.genadidharma.github.model.UserFollowingsItem
import com.genadidharma.github.webservice.GithubAPIService
import java.lang.Exception

class UserFollowingsRemoteDataSource(private val githubAPIService: GithubAPIService) :
    UserFollowingsDataSource {
    override suspend fun getFollowings(username: String): MutableList<UserFollowingsItem>? {
        val response = githubAPIService.getUserFollowings(username);
        if (response.isSuccessful) return response.body()?.items

        throw Exception("${response.code()}: ${response.message()}")
    }

    override suspend fun addAll(username: String, followings: MutableList<UserFollowingsItem>?) {
        TODO("Not yet implemented")
    }
}
