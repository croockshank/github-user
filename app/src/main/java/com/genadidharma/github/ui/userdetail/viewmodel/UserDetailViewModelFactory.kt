package com.genadidharma.github.ui.userdetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.genadidharma.github.repository.UserDetailRepository

class UserDetailViewModelFactory(
    private val userDetailRepository: UserDetailRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserDetailViewModel::class.java)) {
            return UserDetailViewModel(userDetailRepository) as T
        }
        throw IllegalArgumentException()
    }

}