package com.example.inhabitnow.data.data_source.tag

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.data_source.base.BaseDataSource
import com.example.inhabitnow.database.InhabitNowDatabase
import database.TagTable
import kotlinx.coroutines.CoroutineDispatcher

class DefaultTagDataSource(
    private val db: InhabitNowDatabase,
    private val ioDispatcher: CoroutineDispatcher
) : BaseDataSource(db, ioDispatcher), TagDataSource {

    private val tagDao = db.tagDaoQueries

    override suspend fun insertTag(tagTable: TagTable): ResultModel<Unit> = runQuery {
        tagDao.insertTag(tagTable)
    }

}