package com.genadidharma.github.ui.userfollowings.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genadidharma.github.repository.UserFollowingsRepository
import com.genadidharma.github.ui.userfollowings.UserFollowingsViewState
import kotlinx.coroutines.launch

class UserFollowingsViewModel(private val userFollowingsRepository: UserFollowingsRepository) : ViewModel() {
    private val mViewState = MutableLiveData<UserFollowingsViewState>().apply {
        value = UserFollowingsViewState(loading = true)
    }

    val viewState: LiveData<UserFollowingsViewState>
        get() = mViewState

    fun getFollowings(username: String) = viewModelScope.launch {
        try {
            val data = userFollowingsRepository.getFollowings(username)
            mViewState.value = mViewState.value?.copy(loading = false, error = null, data = data)
        }catch (e: Exception){
            mViewState.value = mViewState.value?.copy(loading = false, error = e, data = null)
        }
    }
}