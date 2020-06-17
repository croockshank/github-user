package com.genadidharma.github.ui.usersearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genadidharma.github.repository.UserSearchRepository
import com.genadidharma.github.ui.usersearch.UserSearchViewState
import kotlinx.coroutines.launch

class UserSearchViewModel(
    private val userSearchRepository: UserSearchRepository
) : ViewModel() {
    private val mViewState = MutableLiveData<UserSearchViewState>()

    var username: String? = null
        set(value) {
            field = value
            mViewState.apply {
                this.value = UserSearchViewState(loading = true)
            }
            username?.let { getUsers(it) }
        }

    val viewState: LiveData<UserSearchViewState>
        get() = mViewState

    fun getUsers(username: String) = viewModelScope.launch {
        try {
            val data = userSearchRepository.getUsers(username)
            mViewState.value = mViewState.value?.copy(loading = false, error = null, data = data)
        } catch (e: Exception) {
            mViewState.value = mViewState.value?.copy(loading = false, error = e, data = null)
        }
    }
}
