package com.genadidharma.github.ui.userfollowers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.genadidharma.github.repository.UserFollowersRepository

class UserFollowersViewModelFactory(
    private val userFollowersRepository: UserFollowersRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserFollowersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserFollowersViewModel(userFollowersRepository) as T
        }
        throw IllegalArgumentException()
    }
}