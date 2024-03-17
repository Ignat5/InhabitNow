package com.example.inhabitnow.domain.use_case.update_task_frequency_by_id

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.core.util.randomUUID
import com.example.inhabitnow.data.model.task.content.FrequencyContentEntity
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import com.example.inhabitnow.domain.strategy.UpdateTaskContentStrategy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class DefaultUpdateTaskFrequencyByIdUseCase(
    private val taskRepository: TaskRepository,
    private val defaultDispatcher: CoroutineDispatcher,
    private val externalScope: CoroutineScope
) : UpdateTaskFrequencyByIdUseCase {

    private val strategy = UpdateTaskContentStrategy()

//    suspend operator fun invoke(
//        taskId: String,
//        frequencyContent: TaskContentModel.FrequencyContent
//    ) = withContext(defaultDispatcher) {
//        strategy(
//            getTaskContent = {
//                taskRepository.getTaskFrequencyContentByTaskId(taskId)
//            },
//            saveTaskContent = { startDate ->
//                val result = taskRepository.saveTaskFrequencyContent(
//                    frequencyContentEntity = FrequencyContentEntity(
//                        id = randomUUID(),
//                        taskId = taskId,
//                        content = frequencyContent,
//                        startDate = startDate,
//                        createdAt = Clock.System.now().toEpochMilliseconds()
//                    )
//                )
//                if (result is ResultModel.Success) {
//                    // TODO(set up reminders)
//                }
//                result
//            },
//            updateTaskContent =
//        )
//    }

}