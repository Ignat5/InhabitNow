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

    override suspend operator fun invoke(requestType: SaveDefaultTaskUseCase.RequestType): ResultModel<String> =
        withContext(defaultDispatcher) {
            val startDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val resultModel = taskRepository.saveDefaultTaskWithContent(
                taskType = when (requestType) {
                    is SaveDefaultTaskUseCase.RequestType.CreateHabit -> TaskType.Habit
                    is SaveDefaultTaskUseCase.RequestType.CreateRecurringTask -> TaskType.RecurringTask
                    is SaveDefaultTaskUseCase.RequestType.CreateTask -> TaskType.SingleTask
                },
                taskProgressType = when (requestType) {
                    is SaveDefaultTaskUseCase.RequestType.CreateHabit -> requestType.taskProgressType
                    is SaveDefaultTaskUseCase.RequestType.CreateRecurringTask -> TaskProgressType.YesNo
                    is SaveDefaultTaskUseCase.RequestType.CreateTask -> TaskProgressType.YesNo
                },
                title = DomainConst.DEFAULT_TASK_TITLE,
                description = DomainConst.DEFAULT_TASK_DESCRIPTION,
                startDate = startDate,
                endDate = when (requestType) {
                    is SaveDefaultTaskUseCase.RequestType.CreateHabit -> null
                    is SaveDefaultTaskUseCase.RequestType.CreateRecurringTask -> null
                    is SaveDefaultTaskUseCase.RequestType.CreateTask -> startDate
                },
                priority = DomainConst.DEFAULT_PRIORITY,
                progressContent = when (requestType) {
                    is SaveDefaultTaskUseCase.RequestType.CreateHabit -> {
                        when (requestType.taskProgressType) {
                            TaskProgressType.YesNo -> TaskContentEntity.ProgressContent.YesNo
                            TaskProgressType.Number -> TaskContentEntity.ProgressContent.Number(
                                limitType = DomainConst.DEFAULT_LIMIT_TYPE,
                                limitNumber = DomainConst.DEFAULT_LIMIT_NUMBER,
                                limitUnit = DomainConst.DEFAULT_LIMIT_UNIT
                            )

                            TaskProgressType.Time -> TaskContentEntity.ProgressContent.Time(
                                limitType = DomainConst.DEFAULT_LIMIT_TYPE,
                                limitTime = DomainConst.DEFAULT_LIMIT_TIME
                            )
                        }
                    }

                    is SaveDefaultTaskUseCase.RequestType.CreateRecurringTask -> TaskContentEntity.ProgressContent.YesNo
                    is SaveDefaultTaskUseCase.RequestType.CreateTask -> TaskContentEntity.ProgressContent.YesNo
                },
                frequencyContent = when (requestType) {
                    is SaveDefaultTaskUseCase.RequestType.CreateHabit,
                    is SaveDefaultTaskUseCase.RequestType.CreateRecurringTask -> {
                        TaskContentEntity.FrequencyContent.EveryDay
                    }

                    is SaveDefaultTaskUseCase.RequestType.CreateTask -> TaskContentEntity.FrequencyContent.OneDay
                },
                archiveContent = TaskContentEntity.ArchiveContent(isArchived = DomainConst.DEFAULT_IS_ARCHIVED)
            )
            resultModel
        }

}