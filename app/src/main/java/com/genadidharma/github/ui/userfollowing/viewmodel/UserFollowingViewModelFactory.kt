package com.genadidharma.github.ui.userfollowing.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.genadidharma.github.repository.UserFollowingRepository

class UserFollowingViewModelFactory(
    private val userFollowingRepository: UserFollowingRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserFollowingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserFollowingViewModel(
                userFollowingRepository
            ) as T
        }
        throw IllegalArgumentException()
    }
}