package com.example.inhabitnow.domain.use_case.archive_task_by_id

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.model.task.content.TaskContentEntity
import com.example.inhabitnow.data.repository.task.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DefaultArchiveTaskByIdUseCase(
    private val taskRepository: TaskRepository,
    private val externalScope: CoroutineScope
) : ArchiveTaskByIdUseCase {

    override suspend operator fun invoke(taskId: String, archive: Boolean): ResultModel<Unit> {
        val targetDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val resultModel = taskRepository.saveTaskArchiveContent(
            taskId = taskId,
            targetDate = targetDate,
            content = TaskContentEntity.ArchiveContent(isArchived = archive)
        )
        if (resultModel is ResultModel.Success) {
            if (archive) {
                externalScope.launch {
                    // reset reminders
                }
            } else {
                externalScope.launch {
                    // set up reminders
                }
            }
        }
        return resultModel
    }

}