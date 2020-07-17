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
abstract class GithubDatabaseApplication : RoomDatabase() {
    companion object {
        private var instance: GithubDatabaseApplication? = null
        fun getInstance(context: Context): GithubDatabaseApplication {
            instance?.let {
                return it
            }
            instance = Room.databaseBuilder(
                context,
                GithubDatabaseApplication::class.java,
                "db_github"
            )
                .fallbackToDestructiveMigration()
                .build()
            return instance!!
        }
    }

    abstract fun userSearchDao(): UserSearchDao
    abstract fun userSearchRemoteKeyDao(): UserSearchRemoteKeyDao

}