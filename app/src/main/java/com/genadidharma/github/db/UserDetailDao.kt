package com.genadidharma.github.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.genadidharma.github.model.UserDetailItem

@Dao
interface UserDetailDao {
    @Query("SELECT * FROM UserDetailItem")
    suspend fun getAll(): MutableList<UserDetailItem>

    @Query("DELETE FROM UserDetailItem")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(vararg userDetailItem: UserDetailItem)

}
