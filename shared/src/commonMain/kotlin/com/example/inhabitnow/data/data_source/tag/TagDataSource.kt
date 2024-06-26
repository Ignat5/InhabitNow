package com.example.inhabitnow.data.data_source.tag

import com.example.inhabitnow.core.model.ResultModel
import database.TagTable
import kotlinx.coroutines.flow.Flow

interface TagDataSource {
    fun readTags(): Flow<List<TagTable>>
    fun readTagIdsByTaskId(taskId: String): Flow<List<String>>
    suspend fun insertTag(tagTable: TagTable): ResultModel<Unit>
    suspend fun updateTagById(tagId: String, tagTitle: String): ResultModel<Unit>
    suspend fun deleteTagById(tagId: String): ResultModel<Unit>
    suspend fun insertTagCrossByTaskId(taskId: String, allTagIds: List<String>): ResultModel<Unit>
}