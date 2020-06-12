package com.genadidharma.github.ui.userdetail

import com.genadidharma.github.model.UserDetailItem
import java.lang.Exception

data class UserDetailViewState(
    val loading: Boolean = false,
    val error: Exception? = null,
    val data: UserDetailItem? = null
)