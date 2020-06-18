package com.genadidharma.github.ui.userfollowing.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genadidharma.github.repository.UserFollowingRepository
import com.genadidharma.github.ui.userfollowing.UserFollowingViewState
import kotlinx.coroutines.launch

class UserFollowingViewModel(private val userFollowingRepository: UserFollowingRepository) : ViewModel() {
    private val mViewState = MutableLiveData<UserFollowingViewState>().apply {
        value = UserFollowingViewState(loading = true)
    }

    val viewState: LiveData<UserFollowingViewState>
        get() = mViewState

    fun getFollowings(username: String) = viewModelScope.launch {
        try {
            val data = userFollowingRepository.getFollowings(username)
            mViewState.value = mViewState.value?.copy(loading = false, error = null, data = data)
        }catch (e: Exception){
            mViewState.value = mViewState.value?.copy(loading = false, error = e, data = null)
        }
    }
}