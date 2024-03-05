package com.example.inhabitnow.data.data_source.tag

import com.example.inhabitnow.core.model.ResultModel
import database.TagTable

interface TagDataSource {
    suspend fun insertTag(tagTable: TagTable): ResultModel<Unit>
}