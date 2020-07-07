package com.genadidharma.github.util

import android.app.Application
import com.genadidharma.github.datastore.userdetail.UserDetailRemoteDataStore
import com.genadidharma.github.datastore.userfavorites.UserFavoritesRoomDataStore
import com.genadidharma.github.datastore.userfollowers.UserFollowersRemoteDataSource
import com.genadidharma.github.datastore.userfollowings.UserFollowingRemoteDataSource
import com.genadidharma.github.datastore.usersearch.UserSearchRemoteDataStore
import com.genadidharma.github.db.DatabaseApplication
import com.genadidharma.github.repository.*
import com.genadidharma.github.webservice.RetrofitApp

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val githubAPIService = RetrofitApp.GITHUB_API_SERVICE
        val databaseApplication = DatabaseApplication.getInstance(this)

        UserSearchRepository.instance.apply {
            init(UserSearchRemoteDataStore(githubAPIService))
        }

        UserDetailRepository.instance.apply {
            init(UserDetailRemoteDataStore(githubAPIService))
        }

        UserFollowersRepository.instance.apply {
            init(UserFollowersRemoteDataSource(githubAPIService))
        }

        UserFollowingRepository.instance.apply {
            init(UserFollowingRemoteDataSource(githubAPIService))
        }
        UserFavoriteRepository.instance.apply {
            initLocalOnly(UserFavoritesRoomDataStore(databaseApplication.userFavoriteDao()))
        }
    }
}