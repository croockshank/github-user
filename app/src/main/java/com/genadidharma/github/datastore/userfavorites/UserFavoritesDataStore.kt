package com.genadidharma.github.datastore.userfavorites

import com.genadidharma.github.model.UserFavoriteItem

interface UserFavoritesDataStore {
    suspend fun getUsers(): MutableList<UserFavoriteItem>?
    suspend fun addToFavorite(userFavorite: UserFavoriteItem)
    suspend fun removeFromFavorite(userFavorite: UserFavoriteItem)
}