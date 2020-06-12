package com.genadidharma.github.webservice

import com.genadidharma.github.BuildConfig
import com.genadidharma.github.model.UserDetailItem
import com.genadidharma.github.model.UserSearchItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubAPIService {
    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.Github_API_KEY}")
    suspend fun getUsers(@Query("q") username: String): Response<UserSearchItem.UserSearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.Github_API_KEY}")
    suspend fun getUserDetail(@Path("username") username: String): Response<UserDetailItem>
}
