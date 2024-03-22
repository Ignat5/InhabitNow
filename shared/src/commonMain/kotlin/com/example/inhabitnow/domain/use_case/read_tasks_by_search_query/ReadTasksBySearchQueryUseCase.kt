package com.example.inhabitnow.domain.use_case.read_tasks_by_search_query

import com.example.inhabitnow.domain.model.task.TaskWithContentModel
import kotlinx.coroutines.flow.Flow

interface ReadTasksBySearchQueryUseCase {
    operator fun invoke(searchQuery: String): Flow<List<TaskWithContentModel>>
}