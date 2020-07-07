package com.genadidharma.github.ui.userfavorites

import com.genadidharma.github.model.UserFavoriteItem
import java.lang.Exception

data class UserFavoritesViewState(
    val loading: Boolean = false,
    val error: Exception? = null,
    val data: MutableList<UserFavoriteItem>? = null,
    val isFavorite: Boolean = false
)