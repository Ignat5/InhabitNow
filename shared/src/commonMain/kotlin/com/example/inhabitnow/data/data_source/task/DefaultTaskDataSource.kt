package com.example.inhabitnow.data.data_source.task

import com.example.inhabitnow.data.data_source.base.BaseDataSource
import com.example.inhabitnow.database.InhabitNowDatabase
import kotlinx.coroutines.CoroutineDispatcher

class DefaultTaskDataSource(
    private val db: InhabitNowDatabase,
    private val ioDispatcher: CoroutineDispatcher
) : BaseDataSource(db, ioDispatcher), TaskDataSource {

}