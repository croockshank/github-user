package com.genadidharma.github.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.genadidharma.github.model.UserSearchItem

@Dao
interface UserSearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: MutableList<UserSearchItem>?)

    @Query("SELECT * FROM users_search ORDER BY indexInResponse ASC")
    fun getUsers(): PagingSource<Int, UserSearchItem>

    @Query("DELETE FROM users_search")
    suspend fun deleteUsers()

    @Query("SELECT MAX(indexInResponse) + 1 FROM users_search")
    suspend fun getNextIndex(): Int
}
