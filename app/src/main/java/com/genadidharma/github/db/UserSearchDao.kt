package com.genadidharma.github.db

import android.database.Cursor
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.genadidharma.github.model.UserSearchItem
import org.threeten.bp.OffsetDateTime

@Dao
interface UserSearchDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(users: MutableList<UserSearchItem>?): List<Long>

    @Query("UPDATE users_search SET isFavorite = :isFavorite, favoriteTimeStamp = :favoriteTimeStamp  WHERE id = :userId")
    suspend fun updateToFavorite(
        isFavorite: Boolean = true,
        favoriteTimeStamp: OffsetDateTime? = OffsetDateTime.now(),
        userId: Int
    )

    @Query("UPDATE users_search SET isFavorite = :isNotFavorite, favoriteTimeStamp = :favoriteTimeStamp WHERE id = :userId")
    suspend fun updateToNotFavorite(
        isNotFavorite: Boolean = false,
        favoriteTimeStamp: OffsetDateTime? = null,
        userId: Int
    )

    @Query(
        "SELECT * FROM users_search WHERE isFavorite = :isFavorite AND login LIKE :keyword " +
                "UNION SELECT * FROM users_search WHERE isFavorite = :isNotFavorite " +
                "ORDER BY favoriteTimeStamp DESC, indexInResponse ASC"
    )
    fun getUsers(
        isNotFavorite: Boolean = false,
        isFavorite: Boolean = true,
        keyword: String
    ): PagingSource<Int, UserSearchItem>

    @Query("SELECT * FROM users_search WHERE isFavorite = :isFavorite ORDER BY favoriteTimeStamp DESC")
    fun getUsersFavorite(isFavorite: Boolean = true): Cursor

    @Query("DELETE FROM users_search WHERE isFavorite = :isNotFavorite")
    suspend fun deleteUsers(isNotFavorite: Boolean = false)

    @Query("SELECT MAX(indexInResponse) + 1 FROM users_search")
    suspend fun getNextIndex(): Int
}
