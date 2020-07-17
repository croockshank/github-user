package com.genadidharma.github.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.genadidharma.github.model.UserSearchItem
import com.genadidharma.github.model.UserSearchRemoteKey

@Database(
    entities = [UserSearchItem::class, UserSearchRemoteKey::class],
    version = 1,
    exportSchema = false
)
abstract class GithubDB : RoomDatabase() {
    companion object {
        fun create(context: Context): GithubDB {
            val databaseBuilder = Room.databaseBuilder(
                context,
                GithubDB::class.java,
                "github"
            )
            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun userSearchDao(): UserSearchDao
    abstract fun userSearchRemoteKeyDao(): UserSearchRemoteKeyDao

}