package com.example.inhabitnow.domain.use_case.update_task_progress_by_id

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.core.util.randomUUID
import com.example.inhabitnow.data.model.task.content.ProgressContentEntity
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import com.example.inhabitnow.domain.strategy.UpdateTaskContentStrategy
import com.example.inhabitnow.domain.util.toProgressContentEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class DefaultUpdateTaskProgressByIdUseCase(
    private val taskRepository: TaskRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : UpdateTaskProgressByIdUseCase {

    private val strategy = UpdateTaskContentStrategy()

    override suspend operator fun invoke(
        taskId: String,
        progressContent: TaskContentModel.ProgressContent
    ): ResultModel<Unit> = withContext(defaultDispatcher) {
        strategy(
            getTaskContent = {
                taskRepository.getTaskProgressContentByTaskId(taskId)
            },
            saveTaskContent = { startDate ->
                taskRepository.saveTaskProgressContent(
                    progressContentEntity = ProgressContentEntity(
                        id = randomUUID(),
                        taskId = taskId,
                        content = progressContent.toProgressContentEntity(),
                        startDate = startDate,
                        createdAt = Clock.System.now().toEpochMilliseconds()
                    )
                )
            },
            updateTaskContent = { contentId ->
                taskRepository.updateTaskProgressContentById(
                    contentId = contentId,
                    progressContent = progressContent.toProgressContentEntity()
                )
            }
        )
    }

}