package com.genadidharma.github.ui.usersearch

import com.genadidharma.github.model.UserSearchItem
import java.lang.Exception

data class UserSearchViewState(
    val loading: Boolean = false,
    val error: Exception? = null,
    val data: MutableList<UserSearchItem>? = null
)