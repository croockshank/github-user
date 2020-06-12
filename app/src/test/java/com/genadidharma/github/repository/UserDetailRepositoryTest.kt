package com.genadidharma.github.repository

import com.genadidharma.github.datastore.userdetail.UserDetailRemoteDataStore
import com.genadidharma.github.model.UserDetailItem
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class UserDetailRepositoryTest{
    @Mock
    var remoteDataStore: UserDetailRemoteDataStore? = null
    private var userDetailRepository: UserDetailRepository? = null
    private var user = UserDetailItem()

    @Before
    fun init(){
        MockitoAnnotations.initMocks(this)
        userDetailRepository = UserDetailRepository.instance.apply {
            init(remoteDataStore!!)
        }
    }

    @Test
    fun shouldGetDataFromRemote(){
        runBlocking {
            Mockito.`when`(remoteDataStore?.getUser(ArgumentMatchers.anyString())).thenReturn(user)
            userDetailRepository?.getUser("Test search user")

            Mockito.verify(remoteDataStore, Mockito.times(1))
                ?.getUser(ArgumentMatchers.anyString())
        }
    }

    @Test
    fun shouldThrowExceptionWhenRemoteThrowingError(){
        runBlocking {
            Mockito.`when`(remoteDataStore?.getUser(ArgumentMatchers.anyString())).thenAnswer { throw Exception() }

            try{
                userDetailRepository?.getUser("Tes search user")
            }catch (e: Exception){

            }
        }
    }
}