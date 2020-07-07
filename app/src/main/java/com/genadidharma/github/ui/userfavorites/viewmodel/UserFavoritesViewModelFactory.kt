package com.genadidharma.github.ui.userfavorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.genadidharma.github.repository.UserFavoriteRepository
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class UserFavoritesViewModelFactory(private val userFavoriteRepository: UserFavoriteRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserFavoritesViewModel::class.java)){
            return UserFavoritesViewModel(
                userFavoriteRepository
            ) as T
        }
        throw IllegalArgumentException()
    }

}