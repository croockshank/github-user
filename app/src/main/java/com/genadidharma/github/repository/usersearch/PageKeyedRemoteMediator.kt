package com.genadidharma.github.repository.usersearch

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.genadidharma.github.datastore.usersearch.UserSearchDataStore
import com.genadidharma.github.model.UserSearchItem
import com.genadidharma.github.model.UserSearchRemoteKey
import retrofit2.HttpException
import java.io.IOException


@OptIn(ExperimentalPagingApi::class)
class PageKeyedRemoteMediator(
    private val localDataStore: UserSearchDataStore?,
    private val remoteDataStore: UserSearchDataStore?,
    private val keyword: String
) : RemoteMediator<Int, UserSearchItem>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserSearchItem>
    ): MediatorResult {
        try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = localDataStore?.getUsersRemoteKey()

                    if (remoteKey?.nexPageKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    remoteKey.nexPageKey
                }
            }

            val data = remoteDataStore?.getUsers(
                keyword = keyword,
                page = loadKey,
                perPage = when (loadType) {
                    LoadType.REFRESH -> state.config.initialLoadSize
                    else -> state.config.pageSize
                }
            )

            if (loadType == LoadType.REFRESH) {
                localDataStore?.deleteUsers()
                localDataStore?.deleteRemoteKey()
            }

            val nextKey = if (data!!.isEmpty()) null else loadKey + 1

            localDataStore?.insertUsers(data)
            localDataStore?.insertRemoteKey(UserSearchRemoteKey(keyword, nexPageKey = nextKey))

            return MediatorResult.Success(endOfPaginationReached = data.isEmpty())

        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }

    }
}
