package com.genadidharma.github.util

import android.app.Application
import com.facebook.stetho.Stetho
import com.genadidharma.github.datastore.userdetail.UserDetailRemoteDataStore
import com.genadidharma.github.datastore.userfollowers.UserFollowersRemoteDataSource
import com.genadidharma.github.datastore.userfollowings.UserFollowingRemoteDataSource
import com.genadidharma.github.datastore.usersearch.UserSearchRemoteDataStore
import com.genadidharma.github.datastore.usersearch.UserSearchRoomDataStore
import com.genadidharma.github.db.GithubDatabaseApplication
import com.genadidharma.github.repository.UserDetailRepository
import com.genadidharma.github.repository.UserFollowersRepository
import com.genadidharma.github.repository.UserFollowingRepository
import com.genadidharma.github.repository.usersearch.UserSearchRepository
import com.genadidharma.github.webservice.RetrofitApp
import com.jakewharton.threetenabp.AndroidThreeTen

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)
        AndroidThreeTen.init(this)

        val githubAPIService = RetrofitApp.GITHUB_API_SERVICE
        val databaseApplication = GithubDatabaseApplication.getInstance(this)

        UserSearchRepository.instance.apply {
            init(
                UserSearchRoomDataStore(
                    databaseApplication.userSearchDao(),
                    databaseApplication.userSearchRemoteKeyDao()
                ), UserSearchRemoteDataStore(githubAPIService)
            )
        }

        UserDetailRepository.instance.apply {
            init(null, UserDetailRemoteDataStore(githubAPIService))
        }

        UserFollowersRepository.instance.apply {
            init(null, UserFollowersRemoteDataSource(githubAPIService))
        }

        UserFollowingRepository.instance.apply {
            init(null, UserFollowingRemoteDataSource(githubAPIService))
        }
    }
}