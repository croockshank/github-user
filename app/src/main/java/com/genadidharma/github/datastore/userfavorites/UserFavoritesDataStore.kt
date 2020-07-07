package com.genadidharma.github.datastore.userfavorites

import com.genadidharma.github.model.UserFavoriteItem

interface UserFavoritesDataStore {
    suspend fun getFavorites(): MutableList<UserFavoriteItem>?
    suspend fun addToFavorite(userFavorite: UserFavoriteItem)
    suspend fun removeFromFavorite(userFavorite: UserFavoriteItem)
}