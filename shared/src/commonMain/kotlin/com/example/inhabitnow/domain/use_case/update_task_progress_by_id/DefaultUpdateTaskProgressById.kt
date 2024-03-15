package com.example.inhabitnow.domain.use_case.update_task_progress_by_id

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.core.util.randomUUID
import com.example.inhabitnow.data.model.task.content.ProgressContentEntity
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import com.example.inhabitnow.domain.util.toProgressContentEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DefaultUpdateTaskProgressById(
    private val taskRepository: TaskRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : UpdateTaskProgressById {

    override suspend operator fun invoke(
        taskId: String,
        progressContent: TaskContentModel.ProgressContent
    ): ResultModel<Unit> = withContext(defaultDispatcher) {
        val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        taskRepository.getTaskProgressContentByTaskId(taskId)?.let { progressContentEntity ->
            val shouldUpdate = todayDate <= progressContentEntity.startDate
            if (shouldUpdate) {
                taskRepository.updateTaskProgressContentById(
                    contentId = progressContentEntity.id,
                    progressContent = progressContent.toProgressContentEntity()
                )
            } else {
                taskRepository.saveTaskProgressContent(
                    progressContentEntity = ProgressContentEntity(
                        id = randomUUID(),
                        taskId = taskId,
                        content = progressContent.toProgressContentEntity(),
                        startDate = todayDate,
                        createdAt = Clock.System.now().toEpochMilliseconds()
                    )
                )
            }
        } ?: ResultModel.Error(IllegalStateException())
    }

}