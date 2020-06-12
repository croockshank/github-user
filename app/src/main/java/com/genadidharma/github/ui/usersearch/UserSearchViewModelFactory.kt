package com.genadidharma.github.ui.usersearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.genadidharma.github.repository.UserSearchRepository
import java.lang.IllegalArgumentException

class UserSearchViewModelFactory(
    private val userSearchRepository: UserSearchRepository
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserSearchViewModel::class.java)){
            return UserSearchViewModel(UserSearchRepository.instance) as T
        }
        throw IllegalArgumentException()
    }

}