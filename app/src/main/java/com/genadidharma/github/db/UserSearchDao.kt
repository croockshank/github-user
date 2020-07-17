package com.genadidharma.github.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.genadidharma.github.model.UserSearchItem

@Dao
interface UserSearchDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(users: MutableList<UserSearchItem>?)

    @Query("UPDATE users_search SET isFavorite = :isFavorite WHERE id = :userId")
    suspend fun updateToFavorite(isFavorite: Boolean = true, userId: Int)

    @Query("UPDATE users_search SET isFavorite = :isNotFavorite WHERE id = :userId")
    suspend fun updateToNotFavorite(isNotFavorite: Boolean = false, userId: Int)

    @Query("SELECT * FROM users_search WHERE isFavorite = :isNotFavorite " +
            "UNION SELECT * FROM users_search WHERE isFavorite = :isFavorite AND login LIKE :keyword ORDER BY indexInResponse ASC")
    fun getUsers(isNotFavorite: Boolean = false, isFavorite: Boolean = true, keyword: String): PagingSource<Int, UserSearchItem>

    @Query("DELETE FROM users_search WHERE isFavorite = :isNotFavorite")
    suspend fun deleteUsers(isNotFavorite: Boolean = false)

    @Query("SELECT MAX(indexInResponse) + 1 FROM users_search")
    suspend fun getNextIndex(): Int
}
