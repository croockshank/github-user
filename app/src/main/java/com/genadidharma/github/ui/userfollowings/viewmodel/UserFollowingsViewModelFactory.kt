package com.genadidharma.github.ui.userfollowings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.genadidharma.github.repository.UserFollowersRepository
import java.lang.IllegalArgumentException

class UserFollowingsViewModelFactory(
    private val userFollowingsRepository: UserFollowersRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserFollowingsViewModel::class.java)){
            return userFollowingsRepository as T
        }
        throw IllegalArgumentException()
    }
}