package com.genadidharma.github.ui.userfavorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genadidharma.github.model.UserFavoriteItem
import com.genadidharma.github.repository.UserFavoriteRepository
import com.genadidharma.github.ui.userfavorites.UserFavoritesViewState
import kotlinx.coroutines.launch
import java.lang.Exception

class UserFavoritesViewModel(private val userFavoriteRepository: UserFavoriteRepository) :
    ViewModel() {
    private val mViewState = MutableLiveData<UserFavoritesViewState>().apply {
        value = UserFavoritesViewState(loading = true)
    }

    val viewState: LiveData<UserFavoritesViewState>
        get() = mViewState

    fun getFavorites() = viewModelScope.launch {
        try {
            val data = userFavoriteRepository.getUsers()
            mViewState.value = mViewState.value?.copy(loading = false, error = null, data = data)
        }catch (e: Exception){
            mViewState.value = mViewState.value?.copy(loading = false, error = e, data = null)
        }
    }

    fun addToFavorite(userFavoriteItem: UserFavoriteItem) = viewModelScope.launch {
        try {
            userFavoriteRepository.addToFavorite(userFavoriteItem)
            mViewState.value = mViewState.value?.copy(loading = false, error = null, isFavorite = true)
        }catch (e: Exception){
            mViewState.value = mViewState.value?.copy(loading = false, error = e)
        }
    }

    fun removeFromFavorite(userFavoriteItem: UserFavoriteItem) = viewModelScope.launch {
        try {
            userFavoriteRepository.removeFromFavorite(userFavoriteItem)
            mViewState.value = mViewState.value?.copy(loading = false, error = null, isFavorite = false)
        }catch (e: Exception){
            mViewState.value = mViewState.value?.copy(loading = false, error = e)
        }
    }
}