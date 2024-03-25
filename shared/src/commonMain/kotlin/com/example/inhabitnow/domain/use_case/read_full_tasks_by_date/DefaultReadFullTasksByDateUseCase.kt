package com.example.inhabitnow.domain.use_case.read_full_tasks_by_date

import com.example.inhabitnow.data.model.reminder.ReminderEntity
import com.example.inhabitnow.data.model.reminder.content.ReminderContentEntity
import com.example.inhabitnow.data.model.task.content.TaskContentEntity
import com.example.inhabitnow.data.model.task.derived.FullTaskEntity
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.model.task.derived.FullTaskModel
import com.example.inhabitnow.domain.util.toFullTaskModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

class DefaultReadFullTasksByDateUseCase(
    private val taskRepository: TaskRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : ReadFullTasksByDateUseCase {

    override operator fun invoke(targetDate: LocalDate): Flow<List<FullTaskModel>> =
        taskRepository.readFullTasksByDate(targetDate).map { allFullTasks ->
            if (allFullTasks.isNotEmpty()) {
                withContext(defaultDispatcher) {
                    allFullTasks
                        .filterTasks(targetDate)
                        .map { fullTask ->
                            async {
                                fullTask.copy(
                                    allReminders = fullTask.allReminders.filterReminders(targetDate)
                                )
                            }
                        }.awaitAll()
                        .map { fullTask ->
                            async {
                                fullTask.toFullTaskModel()
                            }
                        }.awaitAll()
                }
            } else emptyList()
        }

    private fun List<FullTaskEntity>.filterTasks(targetDate: LocalDate) = this.let { allFullTasks ->
        allFullTasks.filter {
            it.taskWithContentEntity.let { taskWithContent ->
                val isDeleted = taskWithContent.task.deletedAt != null
                val isArchived = taskWithContent.archiveContent.content.isArchived
                val isScheduled = when (val fc = taskWithContent.frequencyContent.content) {
                    is TaskContentEntity.FrequencyContent.EveryDay -> true
                    is TaskContentEntity.FrequencyContent.OneDay -> {
                        taskWithContent.task.startDate == targetDate
                    }

                    is TaskContentEntity.FrequencyContent.DaysOfWeek -> {
                        targetDate.dayOfWeek in fc.daysOfWeek
                    }
                }
                !isDeleted && !isArchived && isScheduled
            }
        }
    }

    private fun List<ReminderEntity>.filterReminders(targetDate: LocalDate) =
        this.let { allReminders ->
            allReminders.filter { reminder ->
                when (val sc = reminder.schedule) {
                    is ReminderContentEntity.ScheduleContent.EveryDay -> true
                    is ReminderContentEntity.ScheduleContent.DaysOfWeek -> {
                        targetDate.dayOfWeek in sc.daysOfWeek
                    }
                }
            }
        }

}