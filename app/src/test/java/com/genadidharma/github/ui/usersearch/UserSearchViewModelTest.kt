package com.genadidharma.github.ui.usersearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.genadidharma.github.repository.UserSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.lang.Exception

class UserSearchViewModelTest{
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    var userSearchRepository: UserSearchRepository? = null

    private var userSearchViewModel: UserSearchViewModel? = null

    @Before
    fun init(){
        MockitoAnnotations.initMocks(this)
        userSearchViewModel = UserSearchViewModel(userSearchRepository!!)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun shouldLoadingWhenFirstInitialized(){
        val state = userSearchViewModel!!.viewState.value!!
        assertTrue(state.loading)
        assertNull(state.error)
        assertNull(state.data)
    }

    @Test
    fun shouldStopLoadingAndReturnDataWhenSuccess(){
        runBlocking {
            `when`(userSearchRepository?.getUsers(anyString())).thenReturn(mutableListOf())
            userSearchViewModel?.getUsers(anyString())
            val state = userSearchViewModel!!.viewState.value!!
            assertFalse(state.loading)
            assertNull(state.error)
            assertNotNull(state.data)
        }
    }

    @Test
    fun shouldThrowErrorWhenRepositoryIsThrowingError(){
        runBlocking {
            `when`(userSearchRepository?.getUsers(anyString())).thenAnswer { throw Exception() }
            userSearchViewModel?.getUsers(anyString())
            val state = userSearchViewModel!!.viewState.value!!
            assertFalse(state.loading)
            assertNotNull(state.error)
            assertNull(state.data)
        }
    }
}