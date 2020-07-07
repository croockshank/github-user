package com.genadidharma.github.datastore.userfavorites

import com.genadidharma.github.db.UserFavoritesDao
import com.genadidharma.github.model.UserFavoriteItem

class UserFavoritesRoomDataStore(private val userFavoriteDao: UserFavoritesDao) : UserFavoritesDataStore {
    override suspend fun getFavorites(): MutableList<UserFavoriteItem>? {
        val response = userFavoriteDao.getFavorites()
        return if (response.isEmpty()) null else response
    }

    override suspend fun addToFavorite(userFavorite: UserFavoriteItem) {
        userFavorite.let { userFavoriteDao.addToFavorite(userFavorite.copy(isFavorite = true)) }
    }

    override suspend fun removeFromFavorite(login: String) {
        login.let { userFavoriteDao.removeFromFavorite(login) }
    }

}