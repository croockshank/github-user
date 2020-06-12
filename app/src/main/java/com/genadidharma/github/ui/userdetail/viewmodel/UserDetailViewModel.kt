package com.genadidharma.github.ui.userdetail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genadidharma.github.repository.UserDetailRepository
import com.genadidharma.github.ui.userdetail.UserDetailViewState
import kotlinx.coroutines.launch

class UserDetailViewModel(
    private val userDetailRepository: UserDetailRepository
) : ViewModel() {
    private val mViewState = MutableLiveData<UserDetailViewState>().apply {
        value = UserDetailViewState(loading = true)
    }

    val viewState: LiveData<UserDetailViewState>
        get() = mViewState

    fun getUser(username: String) = viewModelScope.launch {
        try {
            val data = userDetailRepository.getUser(username)
            mViewState.value = mViewState.value?.copy(loading = false, error = null, data = data)
        }catch (e: Exception){
            mViewState.value = mViewState.value?.copy(loading = false, error = e, data = null)
        }
    }
}