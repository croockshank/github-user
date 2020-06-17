package com.genadidharma.github.util

import android.app.Application
import com.genadidharma.github.datastore.userdetail.UserDetailRemoteDataStore
import com.genadidharma.github.datastore.userfollowers.UserFollowersRemoteDataSource
import com.genadidharma.github.datastore.userfollowers.UserFollowingsRemoteDataSource
import com.genadidharma.github.datastore.usersearch.UserSearchRemoteDataStore
import com.genadidharma.github.repository.UserDetailRepository
import com.genadidharma.github.repository.UserFollowersRepository
import com.genadidharma.github.repository.UserFollowingsRepository
import com.genadidharma.github.repository.UserSearchRepository
import com.genadidharma.github.webservice.RetrofitApp

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val githubAPIService = RetrofitApp.GITHUB_API_SERVICE

        UserSearchRepository.instance.apply {
            init(UserSearchRemoteDataStore(githubAPIService))
        }

        UserDetailRepository.instance.apply {
            init(UserDetailRemoteDataStore(githubAPIService))
        }

        UserFollowersRepository.instance.apply {
            init(UserFollowersRemoteDataSource(githubAPIService))
        }

        UserFollowingsRepository.instance.apply {
            init(UserFollowingsRemoteDataSource(githubAPIService))
        }
    }
}