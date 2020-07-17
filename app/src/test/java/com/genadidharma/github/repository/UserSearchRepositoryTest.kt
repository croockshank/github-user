package com.genadidharma.github.repository

import com.genadidharma.github.datastore.usersearch.UserSearchRemoteDataStore
import com.genadidharma.github.model.UserSearchItem
import com.genadidharma.github.repository.usersearch.UserSearchRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.lang.Exception

class UserSearchRepositoryTest{
    @Mock
    var remoteDataStore: UserSearchRemoteDataStore? = null
    private var userSearchRepository: UserSearchRepository? = null
    private var users = mutableListOf<UserSearchItem>()

    @Before
    fun init(){
        MockitoAnnotations.initMocks(this)
        userSearchRepository = UserSearchRepository.instance.apply {
            init(remoteDataStore!!)
        }
    }

    @Test
    fun shouldGetDataFromRemote(){
        runBlocking {
            `when`(remoteDataStore?.getUsers(anyString())).thenReturn(users)
            userSearchRepository?.getUsers("Test search user")

            verify(remoteDataStore, times(1))?.getUsers(anyString())
        }
    }

    @Test
    fun shouldThrowExceptionWhenRemoteThrowingError(){
        runBlocking {
            `when`(remoteDataStore?.getUsers(anyString())).thenAnswer { throw Exception() }

            try{
                userSearchRepository?.getUsers("Tes search user")
            }catch (e: Exception){

            }
        }
    }
}