package com.genadidharma.github.ui.userfollowers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.genadidharma.github.repository.UserFollowersRepository
import java.lang.IllegalArgumentException

class UserFollowersViewModelFactory(
    private val userFollowersRepository: UserFollowersRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserFollowersViewModelFactory::class.java)){
            return userFollowersRepository as T
        }
        throw IllegalArgumentException()
    }
}