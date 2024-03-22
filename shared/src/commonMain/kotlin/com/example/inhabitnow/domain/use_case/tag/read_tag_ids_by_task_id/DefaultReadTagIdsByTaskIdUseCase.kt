package com.example.inhabitnow.domain.use_case.tag.read_tag_ids_by_task_id

import com.example.inhabitnow.data.repository.tag.TagRepository
import kotlinx.coroutines.flow.Flow

class DefaultReadTagIdsByTaskIdUseCase(
    private val tagRepository: TagRepository
) : ReadTagIdsByTaskIdUseCase {

    override operator fun invoke(taskId: String): Flow<Set<String>> =
        tagRepository.readTagIdsByTaskId(taskId)

}