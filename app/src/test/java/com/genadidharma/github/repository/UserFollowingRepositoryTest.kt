package com.genadidharma.github.repository

import com.genadidharma.github.datastore.userfollowers.UserFollowingsRemoteDataSource
import com.genadidharma.github.model.UserFollowingsItem
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class UserFollowingRepositoryTest{
    @Mock
    var remoteDataStore: UserFollowingsRemoteDataSource? = null
    private var userFollowingsRepository: UserFollowingsRepository? = null
    private var users = mutableListOf<UserFollowingsItem>()

    @Before
    fun init(){
        MockitoAnnotations.initMocks(this)
        userFollowingsRepository = UserFollowingsRepository.instance.apply {
            init(remoteDataStore!!)
        }
    }

    @Test
    fun shouldGetDataFromRemote(){
        runBlocking {
            Mockito.`when`(remoteDataStore?.getFollowings(ArgumentMatchers.anyString())).thenReturn(users)
            userFollowingsRepository?.getFollowings("Test search user")

            Mockito.verify(remoteDataStore, Mockito.times(1))
                ?.getFollowings(ArgumentMatchers.anyString())
        }
    }

    @Test
    fun shouldThrowExceptionWhenRemoteThrowingError(){
        runBlocking {
            Mockito.`when`(remoteDataStore?.getFollowings(ArgumentMatchers.anyString())).thenAnswer { throw Exception() }

            try{
                userFollowingsRepository?.getFollowings("Tes search user")
            }catch (e: Exception){

            }
        }
    }
}