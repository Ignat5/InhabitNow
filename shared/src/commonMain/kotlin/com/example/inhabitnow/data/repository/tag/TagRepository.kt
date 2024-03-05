package com.example.inhabitnow.data.repository.tag

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.model.tag.TagModel

interface TagRepository {
    suspend fun saveTag(tagModel: TagModel): ResultModel<Unit>
}