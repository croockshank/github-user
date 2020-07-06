package com.genadidharma.github.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.genadidharma.github.model.UserSearchItem

@Dao
interface UserSearchDao {
    @Query("SELECT * FROM UserSearchItem")
    suspend fun getAll(): MutableList<UserSearchItem>

    @Query("DELETE FROM UserSearchItem")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll()
}