package com.genadidharma.github.webservice

import com.genadidharma.github.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitApp {
    private const val BASE_URL = "https://api.github.com/"

    private val client = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val GITHUB_API_SERVICE: GithubAPIService = client.create(GithubAPIService::class.java)
}