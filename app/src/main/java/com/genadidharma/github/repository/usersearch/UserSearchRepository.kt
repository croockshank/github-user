package com.genadidharma.github.repository.usersearch

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.genadidharma.github.datastore.usersearch.UserSearchDataStore
import com.genadidharma.github.model.UserSearchItem
import com.genadidharma.github.repository.BaseRepository
import kotlinx.coroutines.flow.Flow

class UserSearchRepository private constructor() : BaseRepository<UserSearchDataStore>() {
    fun getUsersFromPaging(keyword: String, pageSize: Int): Flow<PagingData<UserSearchItem>> {
        val pagingSourceFactory = { localDataStore!!.getUsersFromDB(keyword) }
        return Pager(
            config = PagingConfig(
                pageSize,
                enablePlaceholders = false
            ),
            remoteMediator = PageKeyedRemoteMediator(localDataStore, remoteDataStore, keyword),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    suspend fun updateToFavorite(userId: Int) {
        localDataStore?.updateToFavorite(userId)
    }

    suspend fun updateToNotFavorite(userId: Int) {
        localDataStore?.updateToNotFavorite(userId)
    }

    companion object {
        val instance by lazy { UserSearchRepository() }
    }
}