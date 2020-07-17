package com.genadidharma.github.ui.usersearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.genadidharma.github.repository.usersearch.UserSearchRepository
import com.genadidharma.github.ui.usersearch.UserSearchViewState
import com.genadidharma.github.ui.util.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class UserSearchViewModel(private val userSearchRepository: UserSearchRepository) : ViewModel() {
    private var mViewState = MutableLiveData<UserSearchViewState>()

    var keyword: String? = null
        set(value) {
            field = value
            mViewState.apply {
                this.value = UserSearchViewState(loading = true)
            }
            keyword?.let { getUsers(it) }
        }

    val viewState: LiveData<UserSearchViewState>
        get() = mViewState

    private fun getUsers(username: String) = viewModelScope.launch {
        val data = userSearchRepository.getUsersFromPaging(username, Constants.PER_PAGE_ITEM_COUNT)
            .cachedIn(viewModelScope)
        mViewState.value =
            mViewState.value?.copy(data = data)
    }
}
