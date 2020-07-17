package com.genadidharma.github.ui.usersearch

import androidx.paging.PagingData
import com.genadidharma.github.model.UserSearchItem
import kotlinx.coroutines.flow.Flow

data class UserSearchViewState(
    val loading: Boolean = false,
    val error: Throwable? = null,
    val data: Flow<PagingData<UserSearchItem>>? = null
)