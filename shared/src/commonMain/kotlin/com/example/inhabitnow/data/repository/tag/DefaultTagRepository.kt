package com.example.inhabitnow.data.repository.tag

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.data_source.tag.TagDataSource
import com.example.inhabitnow.data.model.tag.TagEntity
import com.example.inhabitnow.data.util.toTagEntity
import com.example.inhabitnow.data.util.toTagTable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class DefaultTagRepository(
    private val tagDataSource: TagDataSource,
    private val json: Json,
    private val defaultDispatcher: CoroutineDispatcher
) : TagRepository {

    override fun readTags(): Flow<List<TagEntity>> = tagDataSource.readTags().map { allTags ->
        if (allTags.isNotEmpty()) {
            allTags.map { it.toTagEntity() }
        } else emptyList()
    }

    override fun readTagIdsByTaskId(taskId: String): Flow<List<String>> =
        tagDataSource.readTagIdsByTaskId(taskId)

    override suspend fun saveTag(tagEntity: TagEntity): ResultModel<Unit> =
        withContext(defaultDispatcher) {
            tagDataSource.insertTag(tagEntity.toTagTable())
        }

    override suspend fun updateTagById(tagId: String, tagTitle: String): ResultModel<Unit> =
        tagDataSource.updateTagById(tagId = tagId, tagTitle = tagTitle)

    override suspend fun deleteTagById(tagId: String): ResultModel<Unit> =
        tagDataSource.deleteTagById(tagId)

}