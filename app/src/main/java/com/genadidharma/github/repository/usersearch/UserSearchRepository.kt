package com.genadidharma.github.repository.usersearch

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.genadidharma.github.datastore.usersearch.UserSearchDataStore
import com.genadidharma.github.model.UserSearchItem
import com.genadidharma.github.repository.BaseRepository

class UserSearchRepository private constructor(): BaseRepository<UserSearchDataStore>(){
    fun getUsersFromPaging(keyword: String, pageSize: Int) = Pager(
        config = PagingConfig(pageSize),
        remoteMediator = PageKeyedRemoteMediator(localDataStore, remoteDataStore, keyword)
    ){
        localDataStore!!.getUsersFromDB()
    }.flow

    companion object{
        val instance by lazy { UserSearchRepository() }
    }
}