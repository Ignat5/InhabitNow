package com.example.inhabitnow.domain.use_case.save_default_task

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.core.type.TaskProgressType
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.core.util.randomUUID
import com.example.inhabitnow.data.model.task.TaskEntity
import com.example.inhabitnow.data.model.task.TaskWithContentEntity
import com.example.inhabitnow.data.model.task.content.ArchiveContentEntity
import com.example.inhabitnow.data.model.task.content.FrequencyContentEntity
import com.example.inhabitnow.data.model.task.content.ProgressContentEntity
import com.example.inhabitnow.data.model.task.content.TaskContentEntity
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.util.DomainConst
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DefaultSaveDefaultTaskUseCase(
    private val taskRepository: TaskRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : SaveDefaultTaskUseCase {

    override suspend operator fun invoke(
        taskType: TaskType,
        taskProgressType: TaskProgressType
    ): ResultModel<String> = withContext(defaultDispatcher) {
        val taskId: String = randomUUID()
        val nowInstant = Clock.System.now()
        val nowMillis = nowInstant.toEpochMilliseconds()
        val startDate = nowInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val endDate = when (taskType) {
            TaskType.SingleTask -> startDate
            else -> null
        }
        val task = TaskEntity(
            id = taskId,
            type = taskType,
            progressType = taskProgressType,
            title = DomainConst.DEFAULT_TASK_TITLE,
            description = DomainConst.DEFAULT_TASK_DESCRIPTION,
            startDate = startDate,
            endDate = endDate,
            priority = DomainConst.DEFAULT_PRIORITY.toString(),
            createdAt = nowMillis,
            deletedAt = nowMillis
        )
        val progressContent = ProgressContentEntity(
            id = randomUUID(),
            taskId = taskId,
            content = when (taskProgressType) {
                TaskProgressType.YesNo -> TaskContentEntity.ProgressContent.YesNo
                TaskProgressType.Number -> TaskContentEntity.ProgressContent.Number(
                    limitType = DomainConst.DEFAULT_LIMIT_TYPE,
                    limitNumber = DomainConst.DEFAULT_LIMIT_NUMBER.toString(),
                    limitUnit = DomainConst.DEFAULT_LIMIT_UNIT
                )

                TaskProgressType.Time -> TaskContentEntity.ProgressContent.Time(
                    limitType = DomainConst.DEFAULT_LIMIT_TYPE,
                    limitTime = DomainConst.DEFAULT_LIMIT_TIME
                )
            },
            startDate = startDate,
            createdAt = nowMillis
        )
        val frequencyContent = FrequencyContentEntity(
            id = randomUUID(),
            taskId = taskId,
            content = when (taskType) {
                TaskType.SingleTask -> TaskContentEntity.FrequencyContent.OneDay
                else -> TaskContentEntity.FrequencyContent.EveryDay
            },
            startDate = startDate,
            createdAt = nowMillis
        )
        val archiveContent = ArchiveContentEntity(
            id = randomUUID(),
            taskId = taskId,
            content = TaskContentEntity.ArchiveContent(
                isArchived = DomainConst.DEFAULT_IS_ARCHIVED
            ),
            startDate = startDate,
            createdAt = nowMillis
        )
        val resultModel = taskRepository.saveTaskWithContent(
            taskWithContentEntity = TaskWithContentEntity(
                task = task,
                progressContent = progressContent,
                frequencyContent = frequencyContent,
                archiveContent = archiveContent
            )
        )
        when (resultModel) {
            is ResultModel.Success -> ResultModel.Success(taskId)
            is ResultModel.Error -> ResultModel.Error(resultModel.throwable)
        }
    }

}