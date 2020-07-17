package com.genadidharma.github.repository

abstract class BaseRepository<DataStore>{
    protected var remoteDataStore: DataStore? = null

    fun init(remoteDataStore: DataStore){
        this.remoteDataStore = remoteDataStore
    }
}