package com.genadidharma.github.repository

import com.genadidharma.github.datastore.userfollowers.UserFollowersRemoteDataSource
import com.genadidharma.github.model.UserFollowersItem
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class UserFollowersRepositoryTest{
    @Mock
    var remoteDataStore: UserFollowersRemoteDataSource? = null
    private var userFollowersRepository: UserFollowersRepository? = null
    private var users = mutableListOf<UserFollowersItem>()

    @Before
    fun init(){
        MockitoAnnotations.initMocks(this)
        userFollowersRepository = UserFollowersRepository.instance.apply {
            init(remoteDataStore!!)
        }
    }

    @Test
    fun shouldGetDataFromRemote(){
        runBlocking {
            Mockito.`when`(remoteDataStore?.getFollowers(ArgumentMatchers.anyString())).thenReturn(users)
            userFollowersRepository?.getFollowers("Test search user")

            Mockito.verify(remoteDataStore, Mockito.times(1))
                ?.getFollowers(ArgumentMatchers.anyString())
        }
    }

    @Test
    fun shouldThrowExceptionWhenRemoteThrowingError(){
        runBlocking {
            Mockito.`when`(remoteDataStore?.getFollowers(ArgumentMatchers.anyString())).thenAnswer { throw Exception() }

            try{
                userFollowersRepository?.getFollowers("Tes search user")
            }catch (e: Exception){

            }
        }
    }
}