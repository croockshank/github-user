package com.genadidharma.github.datastore.userfavorites

import com.genadidharma.github.db.UserFavoritesDao
import com.genadidharma.github.model.UserFavoriteItem

class UserFavoritesRoomDataStore(private val userFavoriteDao: UserFavoritesDao) : UserFavoritesDataStore {
    override suspend fun getUsers(): MutableList<UserFavoriteItem>? {
        val response = userFavoriteDao.getFavorites()
        return if (response.isEmpty()) response else null
    }

    override suspend fun addToFavorite(userFavorite: UserFavoriteItem) {
        userFavorite.let { userFavoriteDao.addToFavorite(userFavorite) }
    }

    override suspend fun removeFromFavorite(userFavorite: UserFavoriteItem) {
        userFavorite.let { userFavoriteDao.removeFromFavorite(userFavorite) }
    }

}