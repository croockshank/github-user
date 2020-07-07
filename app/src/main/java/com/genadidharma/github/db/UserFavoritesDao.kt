package com.genadidharma.github.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.genadidharma.github.model.UserFavoriteItem

@Dao
interface UserFavoritesDao {
    @Query("SELECT * FROM UserFavoriteItem ORDER BY favoriteId DESC")
    suspend fun getFavorites(): MutableList<UserFavoriteItem>

    @Query("DELETE FROM UserFavoriteItem WHERE login = :login")
    suspend fun removeFromFavorite(login: String)

    @Insert
    suspend fun addToFavorite(userFavoriteItem: UserFavoriteItem)
}