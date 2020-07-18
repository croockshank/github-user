package com.genadidharma.githubconsumer.ui.util

import android.database.Cursor
import com.genadidharma.githubconsumer.model.UserFavoriteItem

object MappingHelper {
    fun mapCursorToArrayList(userFavoriteCursor: Cursor?): MutableList<UserFavoriteItem> {
        val favoriteList = mutableListOf<UserFavoriteItem>()

        userFavoriteCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(UserFavoriteItem.UserFavoriteItem.ID))
                val avatarUrl =
                    getString(getColumnIndexOrThrow(UserFavoriteItem.UserFavoriteItem.AVATAR_URL))
                val login = getString(getColumnIndex(UserFavoriteItem.UserFavoriteItem.LOGIN))
                val type = getString(getColumnIndex(UserFavoriteItem.UserFavoriteItem.TYPE))
                favoriteList.add(
                    UserFavoriteItem(
                        id = id,
                        avatarUrl = avatarUrl,
                        login = login,
                        type = type
                    )
                )
            }
        }
        return favoriteList
    }
}