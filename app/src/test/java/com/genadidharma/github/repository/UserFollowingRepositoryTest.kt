package com.genadidharma.github.repository

import com.genadidharma.github.datastore.userfollowings.UserFollowingRemoteDataSource
import com.genadidharma.github.model.UserFollowingItem
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class UserFollowingRepositoryTest{
    @Mock
    var remoteDataStore: UserFollowingRemoteDataSource? = null
    private var userFollowingRepository: UserFollowingRepository? = null
    private var users = mutableListOf<UserFollowingItem>()

    @Before
    fun init(){
        MockitoAnnotations.initMocks(this)
        userFollowingRepository = UserFollowingRepository.instance.apply {
            init(remoteDataStore!!)
        }
    }

    @Test
    fun shouldGetDataFromRemote(){
        runBlocking {
            Mockito.`when`(remoteDataStore?.getFollowings(ArgumentMatchers.anyString())).thenReturn(users)
            userFollowingRepository?.getFollowings("Test search user")

            Mockito.verify(remoteDataStore, Mockito.times(1))
                ?.getFollowings(ArgumentMatchers.anyString())
        }
    }

    @Test
    fun shouldThrowExceptionWhenRemoteThrowingError(){
        runBlocking {
            Mockito.`when`(remoteDataStore?.getFollowings(ArgumentMatchers.anyString())).thenAnswer { throw Exception() }

            try{
                userFollowingRepository?.getFollowings("Tes search user")
            }catch (e: Exception){

            }
        }
    }
}