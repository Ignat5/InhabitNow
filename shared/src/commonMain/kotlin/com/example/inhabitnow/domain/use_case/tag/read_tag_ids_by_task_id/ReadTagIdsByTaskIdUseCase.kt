package com.example.inhabitnow.domain.use_case.tag.read_tag_ids_by_task_id

import kotlinx.coroutines.flow.Flow

interface ReadTagIdsByTaskIdUseCase {
    operator fun invoke(taskId: String): Flow<List<String>>
}