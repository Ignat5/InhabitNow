package com.example.inhabitnow.data.repository.tag

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.data_source.tag.TagDataSource
import com.example.inhabitnow.data.model.tag.TagModel
import com.example.inhabitnow.data.util.toTagTable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class DefaultTagRepository(
    private val tagDataSource: TagDataSource,
    private val json: Json,
    private val defaultDispatcher: CoroutineDispatcher
) : TagRepository {

    override suspend fun saveTag(tagModel: TagModel): ResultModel<Unit> =
        withContext(defaultDispatcher) {
            tagDataSource.insertTag(tagModel.toTagTable(json))
        }

}