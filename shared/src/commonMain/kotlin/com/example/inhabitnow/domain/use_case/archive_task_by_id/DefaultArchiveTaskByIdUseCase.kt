package com.example.inhabitnow.domain.use_case.archive_task_by_id

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.model.task.content.TaskContentEntity
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.use_case.reminder.reset_task_reminders.ResetTaskRemindersUseCase
import com.example.inhabitnow.domain.use_case.reminder.set_up_task_reminders.SetUpTaskRemindersUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DefaultArchiveTaskByIdUseCase(
    private val taskRepository: TaskRepository,
    private val setUpTaskRemindersUseCase: SetUpTaskRemindersUseCase,
    private val resetTaskRemindersUseCase: ResetTaskRemindersUseCase,
    private val externalScope: CoroutineScope
) : ArchiveTaskByIdUseCase {

    override suspend operator fun invoke(taskId: String, archive: Boolean): ResultModel<Unit> {
        val targetDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val resultModel =
            taskRepository.getTaskArchiveByTaskId(taskId)?.let { archiveContentEntity ->
                if (targetDate > archiveContentEntity.startDate) {
                    taskRepository.saveTaskArchive(
                        taskId = taskId,
                        archiveContent = TaskContentEntity.ArchiveContent(archive),
                        startDate = targetDate
                    )
                } else {
                    taskRepository.updateTaskArchive(
                        contentId = archiveContentEntity.id,
                        content = TaskContentEntity.ArchiveContent(archive)
                    )
                }
            } ?: ResultModel.Error(NoSuchElementException())
        if (resultModel is ResultModel.Success) {
            if (archive) {
                externalScope.launch {
                    resetTaskRemindersUseCase(taskId)
                }
            } else {
                externalScope.launch {
                    setUpTaskRemindersUseCase(taskId)
                }
            }
        }
        return resultModel
    }

}