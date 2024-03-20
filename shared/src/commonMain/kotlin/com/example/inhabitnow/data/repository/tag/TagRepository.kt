package com.example.inhabitnow.data.repository.tag

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.model.tag.TagEntity
import kotlinx.coroutines.flow.Flow

interface TagRepository {
    fun readTags(): Flow<List<TagEntity>>
    fun readTagIdsByTaskId(taskId: String): Flow<List<String>>
    suspend fun saveTag(tagEntity: TagEntity): ResultModel<Unit>
    suspend fun updateTagById(tagId: String, tagTitle: String): ResultModel<Unit>
    suspend fun deleteTagById(tagId: String): ResultModel<Unit>
}