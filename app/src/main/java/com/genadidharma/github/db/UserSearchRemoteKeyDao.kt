package com.genadidharma.github.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.genadidharma.github.model.UserSearchRemoteKey

@Dao
interface UserSearchRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(remoteKey: UserSearchRemoteKey)

    @Query("SELECT * FROM user_remote_keys")
    suspend fun getRemoteKey(): UserSearchRemoteKey

    @Query("DELETE FROM user_remote_keys")
    suspend fun deleteRemoteKey()
}