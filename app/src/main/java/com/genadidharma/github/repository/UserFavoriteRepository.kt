package com.genadidharma.github.repository

import com.genadidharma.github.datastore.userfavorites.UserFavoritesDataStore
import com.genadidharma.github.model.UserFavoriteItem

class UserFavoriteRepository private constructor() : BaseRepository<UserFavoritesDataStore>() {
    suspend fun getFavorites(): MutableList<UserFavoriteItem>? {
        return localDataStore?.getFavorites()
    }

    suspend fun addToFavorite(userFavoriteItem: UserFavoriteItem) {
        localDataStore?.addToFavorite(userFavoriteItem)
    }

    suspend fun removeFromFavorite(login: String) {
        localDataStore?.removeFromFavorite(login)
    }

    companion object {
        val instance by lazy { UserFavoriteRepository() }
    }
}