package com.genadidharma.github.ui.userfollowing

import com.genadidharma.github.model.UserFollowingItem

data class UserFollowingViewState(
    val loading: Boolean = false,
    val error: Exception? = null,
    val data: MutableList<UserFollowingItem>? = null
)