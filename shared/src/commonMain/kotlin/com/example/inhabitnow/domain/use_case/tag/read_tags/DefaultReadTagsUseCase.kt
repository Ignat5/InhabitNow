package com.example.inhabitnow.domain.use_case.tag.read_tags

import com.example.inhabitnow.data.repository.tag.TagRepository
import com.example.inhabitnow.domain.model.tag.TagModel
import com.example.inhabitnow.domain.util.toTagModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DefaultReadTagsUseCase(
    private val tagRepository: TagRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : ReadTagsUseCase {

    override operator fun invoke(): Flow<List<TagModel>> = tagRepository.readTags().map { allTags ->
        if (allTags.isNotEmpty()) {
            withContext(defaultDispatcher) {
                allTags.map { it.toTagModel() }
            }
        } else emptyList()
    }

}