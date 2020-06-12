package com.genadidharma.github.ui.userfollowings

import com.genadidharma.github.model.UserFollowingsItem

data class UserFollowingsViewState(
    val loading: Boolean = false,
    val error: Exception? = null,
    val data: MutableList<UserFollowingsItem>? = null
)