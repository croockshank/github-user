package com.genadidharma.github.ui.userfollowings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.genadidharma.github.repository.UserFollowingsRepository

class UserFollowingsViewModelFactory(
    private val userFollowingsRepository: UserFollowingsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserFollowingsViewModel::class.java)) {
            return UserFollowingsViewModel(
                userFollowingsRepository
            ) as T
        }
        throw IllegalArgumentException()
    }
}