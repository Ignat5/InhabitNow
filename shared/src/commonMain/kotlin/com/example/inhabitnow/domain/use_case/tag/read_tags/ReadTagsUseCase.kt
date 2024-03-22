package com.example.inhabitnow.domain.use_case.tag.read_tags

import com.example.inhabitnow.domain.model.tag.TagModel
import kotlinx.coroutines.flow.Flow

interface ReadTagsUseCase {
    operator fun invoke(): Flow<List<TagModel>>
}