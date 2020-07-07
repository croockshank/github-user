package com.genadidharma.github.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.genadidharma.github.model.UserDetailItem
import com.genadidharma.github.model.UserFavoriteItem
import com.genadidharma.github.model.UserSearchItem

@Database(
    entities = [UserSearchItem::class, UserDetailItem::class, UserFavoriteItem::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseApplication : RoomDatabase() {
    abstract fun userSearchDao(): UserSearchDao
    abstract fun userDetailDao(): UserDetailDao
    abstract fun userFavoriteDao(): UserFavoritesDao

    companion object {
        private var instance: DatabaseApplication? = null
        fun getInstance(context: Context): DatabaseApplication {
            instance?.let { return it }
            instance = Room.databaseBuilder(
                context.applicationContext,
                DatabaseApplication::class.java,
                "github"
            ).build()
            return instance!!
        }
    }
}