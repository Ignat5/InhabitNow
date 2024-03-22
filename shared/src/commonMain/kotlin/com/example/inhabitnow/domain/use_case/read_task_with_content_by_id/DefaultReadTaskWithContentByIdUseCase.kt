package com.example.inhabitnow.domain.use_case.read_task_with_content_by_id

import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.model.task.TaskWithContentModel
import com.example.inhabitnow.domain.util.toTaskWithContentModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DefaultReadTaskWithContentByIdUseCase(
    private val taskRepository: TaskRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : ReadTaskWithContentByIdUseCase {

    override operator fun invoke(taskId: String): Flow<TaskWithContentModel?> =
        taskRepository.readTaskWithContentById(taskId).map {
            it?.let { taskWithContentEntity ->
                withContext(defaultDispatcher) {
                    taskWithContentEntity.toTaskWithContentModel()
                }
            }
        }

}