package com.example.inhabitnow.domain.task

import com.example.inhabitnow.data.repository.task.TaskRepository
import kotlinx.coroutines.CoroutineDispatcher

class DefaultTaskDomain(
    private val taskRepository: TaskRepository,
    private val defaultDispatcher: CoroutineDispatcher
): TaskDomain {
}