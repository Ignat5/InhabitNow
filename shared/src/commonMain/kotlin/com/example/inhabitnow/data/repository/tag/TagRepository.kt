package com.example.inhabitnow.data.repository.tag

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.model.tag.TagEntity

interface TagRepository {
    suspend fun saveTag(tagEntity: TagEntity): ResultModel<Unit>
}