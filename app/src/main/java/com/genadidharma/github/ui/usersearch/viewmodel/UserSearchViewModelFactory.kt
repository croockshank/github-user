package com.genadidharma.github.ui.usersearch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.genadidharma.github.repository.usersearch.UserSearchRepository

class UserSearchViewModelFactory(
    private val userSearchRepository: UserSearchRepository
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserSearchViewModel::class.java)){
            return UserSearchViewModel(
                userSearchRepository
            ) as T
        }
        throw IllegalArgumentException()
    }

}