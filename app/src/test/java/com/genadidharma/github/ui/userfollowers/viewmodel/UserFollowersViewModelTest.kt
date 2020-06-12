package com.genadidharma.github.ui.userfollowers.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.genadidharma.github.repository.UserFollowersRepository
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

class UserFollowersViewModelTest{
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    var userFollowersRepository: UserFollowersRepository? = null

    private var userFollowersViewModel: UserFollowersViewModel? = null

    @Before
    fun init(){
        MockitoAnnotations.initMocks(this)
        userFollowersViewModel =
            UserFollowersViewModel(
                userFollowersRepository!!
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
            Mockito.`when`(userFollowersRepository?.getFollowers(ArgumentMatchers.anyString())).thenReturn(mutableListOf())
            userFollowersViewModel?.getFollowers(ArgumentMatchers.anyString())
            val state = userFollowersViewModel!!.viewState.value!!
            assertFalse(state.loading)
            assertNull(state.error)
            assertNotNull(state.data)
        }
    }

    @Test
    fun shouldThrowErrorWhenRepositoryIsThrowingError(){
        runBlocking {
            Mockito.`when`(userFollowersRepository?.getFollowers(ArgumentMatchers.anyString())).thenAnswer { throw Exception() }
            userFollowersViewModel?.getFollowers(ArgumentMatchers.anyString())
            val state = userFollowersViewModel!!.viewState.value!!
            assertFalse(state.loading)
            assertNotNull(state.error)
            assertNull(state.data)
        }
    }
}