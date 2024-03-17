package com.example.inhabitnow.domain.use_case.update_task_progress_by_id

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import com.example.inhabitnow.domain.util.toProgressContentEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DefaultUpdateTaskProgressByIdUseCase(
    private val taskRepository: TaskRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : UpdateTaskProgressByIdUseCase {

    override suspend operator fun invoke(
        taskId: String,
        progressContent: TaskContentModel.ProgressContent
    ): ResultModel<Unit> = withContext(defaultDispatcher) {
        val targetDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        taskRepository.saveTaskProgressContent(
            taskId = taskId,
            targetDate = targetDate,
            content = progressContent.toProgressContentEntity()
        )
    }

}