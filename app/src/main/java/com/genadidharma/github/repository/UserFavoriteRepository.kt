package com.genadidharma.github.repository

import com.genadidharma.github.datastore.userfavorites.UserFavoritesDataStore
import com.genadidharma.github.model.UserFavoriteItem

class UserFavoriteRepository private constructor() : BaseRepository<UserFavoritesDataStore>() {
    suspend fun getUsers(): MutableList<UserFavoriteItem>? {
        return localDataStore?.getFavorites()
    }

    suspend fun addToFavorite(userFavoriteItem: UserFavoriteItem) {
        localDataStore?.addToFavorite(userFavoriteItem)
    }

    suspend fun removeFromFavorite(userFavoriteItem: UserFavoriteItem) {
        localDataStore?.removeFromFavorite(userFavoriteItem)
    }

    companion object {
        val instance by lazy { UserFavoriteRepository() }
    }
}