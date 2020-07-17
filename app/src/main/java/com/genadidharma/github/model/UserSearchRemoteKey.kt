package com.genadidharma.github.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_remote_keys")
data class UserSearchRemoteKey (
    @PrimaryKey
    val keyword: String,
    val nexPageKey: Int?
)