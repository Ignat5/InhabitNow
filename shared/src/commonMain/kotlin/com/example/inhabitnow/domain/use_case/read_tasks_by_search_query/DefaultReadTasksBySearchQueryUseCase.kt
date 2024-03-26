package com.example.inhabitnow.domain.use_case.read_tasks_by_search_query

import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.model.task.TaskModel
import com.example.inhabitnow.domain.util.toTaskModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DefaultReadTasksBySearchQueryUseCase(
    private val taskRepository: TaskRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : ReadTasksBySearchQueryUseCase {

    override operator fun invoke(searchQuery: String): Flow<List<TaskModel>> =
        taskRepository.readTasksWithContentBySearchQuery(searchQuery).map { allTasks ->
            if (allTasks.isNotEmpty()) {
                withContext(defaultDispatcher) {
                    allTasks.map { taskWithContent ->
                        taskWithContent.toTaskModel()
                    }.sortedByDescending { it.createdAt }
                }
            } else emptyList()
        }

}