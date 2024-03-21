package com.example.inhabitnow.data.data_source.tag

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.data_source.base.BaseDataSource
import com.example.inhabitnow.database.InhabitNowDatabase
import database.TagCrossTable
import database.TagTable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class DefaultTagDataSource(
    private val db: InhabitNowDatabase,
    private val ioDispatcher: CoroutineDispatcher
) : BaseDataSource(db, ioDispatcher), TagDataSource {

    private val tagDao = db.tagDaoQueries

    override fun readTags(): Flow<List<TagTable>> = readQueryList {
        tagDao.selectTags()
    }

    override fun readTagIdsByTaskId(taskId: String): Flow<List<String>> = readQueryList {
        tagDao.selectTagIdsByTaskId(taskId = taskId)
    }

    override suspend fun insertTag(tagTable: TagTable): ResultModel<Unit> = runQuery {
        tagDao.insertTag(tagTable)
    }

    override suspend fun updateTagById(tagId: String, tagTitle: String): ResultModel<Unit> =
        runQuery {
            tagDao.updateTagById(
                tagId = tagId,
                tagTitle = tagTitle
            )
        }

    override suspend fun deleteTagById(tagId: String): ResultModel<Unit> = runQuery {
        tagDao.deleteTagById(tagId)
    }

    override suspend fun insertTagCrossByTaskId(
        taskId: String,
        allTagIds: List<String>
    ): ResultModel<Unit> = runTransaction {
        tagDao.apply {
            deleteTagCrossByTaskId(taskId)
            allTagIds.forEach { tagId ->
                insertTagCross(
                    TagCrossTable(
                        taskId = taskId,
                        tagId = tagId
                    )
                )
            }
        }
    }

}