package com.genadidharma.github.ui.userdetail.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.genadidharma.github.model.UserDetailItem
import com.genadidharma.github.repository.UserDetailRepository
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

class UserDetailViewModelTest{
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    var userDetailRepository: UserDetailRepository? = null

    private var userDetailViewModel: UserDetailViewModel? = null

    @Before
    fun init(){
        MockitoAnnotations.initMocks(this)
        userDetailViewModel =
            UserDetailViewModel(
                userDetailRepository!!
            )
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun shouldLoadingWhenFirstInitialized(){
        val state = userDetailViewModel!!.viewState.value!!
        assertTrue(state.loading)
        assertNull(state.error)
        assertNull(state.data)
    }

    @Test
    fun shouldStopLoadingAndReturnDataWhenSuccess(){
        runBlocking {
            Mockito.`when`(userDetailRepository?.getUser(ArgumentMatchers.anyString())).thenReturn(
                UserDetailItem()
            )
            userDetailViewModel?.getUser(ArgumentMatchers.anyString())
            val state = userDetailViewModel!!.viewState.value!!
            assertFalse(state.loading)
            assertNull(state.error)
            assertNotNull(state.data)
        }
    }

    @Test
    fun shouldThrowErrorWhenRepositoryIsThrowingError(){
        runBlocking {
            Mockito.`when`(userDetailRepository?.getUser(ArgumentMatchers.anyString())).thenAnswer { throw Exception() }
            userDetailViewModel?.getUser(ArgumentMatchers.anyString())
            val state = userDetailViewModel!!.viewState.value!!
            assertFalse(state.loading)
            assertNotNull(state.error)
            assertNull(state.data)
        }
    }
}