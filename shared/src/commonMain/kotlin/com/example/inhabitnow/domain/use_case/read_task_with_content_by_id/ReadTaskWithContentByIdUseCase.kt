package com.example.inhabitnow.domain.use_case.read_task_with_content_by_id

import com.example.inhabitnow.domain.model.task.TaskModel
import kotlinx.coroutines.flow.Flow

interface ReadTaskWithContentByIdUseCase {

    operator fun invoke(taskId: String): Flow<TaskModel?>

}