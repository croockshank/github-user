package com.genadidharma.github.ui.userfollowers.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genadidharma.github.repository.UserFollowersRepository
import com.genadidharma.github.ui.userfollowers.UserFollowersViewState
import kotlinx.coroutines.launch

class UserFollowersViewModel(private val userFollowersRepository: UserFollowersRepository) : ViewModel() {
    private val mViewState = MutableLiveData<UserFollowersViewState>().apply {
        value = UserFollowersViewState(loading = true)
    }

    val viewState: LiveData<UserFollowersViewState>
        get() = mViewState

    fun getFollowers(username: String) = viewModelScope.launch {
        try {
            val data = userFollowersRepository.getFollowers(username)
            mViewState.value = mViewState.value?.copy(loading = false, error = null, data = data)
        }catch (e: Exception){
            mViewState.value = mViewState.value?.copy(loading = false, error = e, data = null)
        }
    }
}