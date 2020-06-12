package com.genadidharma.github.ui.userfollowers

import com.genadidharma.github.model.UserFollowersItem
import java.lang.Exception

data class UserFollowersViewState(
    val loading: Boolean = false,
    val error: Exception? = null,
    val data: MutableList<UserFollowersItem>? = null
)