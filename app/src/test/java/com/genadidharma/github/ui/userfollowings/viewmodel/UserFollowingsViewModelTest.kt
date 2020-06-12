package com.genadidharma.github.ui.userfollowings.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.genadidharma.github.repository.UserFollowingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class UserFollowingsViewModelTest{
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    var userFollowingsRepository: UserFollowingsRepository? = null

    private var userFollowersViewModel: UserFollowingsViewModel? = null

    @Before
    fun init(){
        MockitoAnnotations.initMocks(this)
        userFollowersViewModel =
            UserFollowingsViewModel(
                userFollowingsRepository!!
            )
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun shouldLoadingWhenFirstInitialized(){
        val state = userFollowersViewModel!!.viewState.value!!
        assertTrue(state.loading)
        assertNull(state.error)
        assertNull(state.data)
    }

    @Test
    fun shouldStopLoadingAndReturnDataWhenSuccess(){
        runBlocking {
            Mockito.`when`(userFollowingsRepository?.getFollowings(ArgumentMatchers.anyString())).thenReturn(mutableListOf())
            userFollowersViewModel?.getFollowings(ArgumentMatchers.anyString())
            val state = userFollowersViewModel!!.viewState.value!!
            assertFalse(state.loading)
            assertNull(state.error)
            assertNotNull(state.data)
        }
    }

    @Test
    fun shouldThrowErrorWhenRepositoryIsThrowingError(){
        runBlocking {
            Mockito.`when`(userFollowingsRepository?.getFollowings(ArgumentMatchers.anyString())).thenAnswer { throw Exception() }
            userFollowersViewModel?.getFollowings(ArgumentMatchers.anyString())
            val state = userFollowersViewModel!!.viewState.value!!
            assertFalse(state.loading)
            assertNotNull(state.error)
            assertNull(state.data)
        }
    }
}