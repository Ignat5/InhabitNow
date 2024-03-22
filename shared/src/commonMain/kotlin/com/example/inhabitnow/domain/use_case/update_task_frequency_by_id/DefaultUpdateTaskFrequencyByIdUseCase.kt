package com.example.inhabitnow.domain.use_case.update_task_frequency_by_id

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import com.example.inhabitnow.domain.util.toFrequencyContentEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DefaultUpdateTaskFrequencyByIdUseCase(
    private val taskRepository: TaskRepository,
    private val defaultDispatcher: CoroutineDispatcher,
    private val externalScope: CoroutineScope
) : UpdateTaskFrequencyByIdUseCase {

    override suspend operator fun invoke(
        taskId: String,
        content: TaskContentModel.FrequencyContent
    ): ResultModel<Unit> = withContext(defaultDispatcher) {
        val targetDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val resultModel = taskRepository.saveTaskFrequencyContent(
            taskId = taskId,
            targetDate = targetDate,
            content = content.toFrequencyContentEntity()
        )
        if (resultModel is ResultModel.Success) {
            externalScope.launch {
//                TODO(set up task's reminders)
            }
        }
        resultModel
    }
}