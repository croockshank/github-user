package com.genadidharma.github.repository

abstract class BaseRepository<DataStore>{
    protected var localDataStore: DataStore? = null
    protected var remoteDataStore: DataStore? = null

    fun init(remoteDataStore: DataStore){
        this.remoteDataStore = remoteDataStore
    }

    fun initLocalOnly(localDataStore: DataStore){
        this.localDataStore = localDataStore
    }
}