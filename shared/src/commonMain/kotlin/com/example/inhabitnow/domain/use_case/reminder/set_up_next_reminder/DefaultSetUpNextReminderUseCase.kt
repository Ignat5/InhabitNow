package com.example.inhabitnow.domain.use_case.reminder.set_up_next_reminder

import com.example.inhabitnow.core.platform.ReminderManager
import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.data.model.reminder.ReminderEntity
import com.example.inhabitnow.data.model.task.TaskWithContentEntity
import com.example.inhabitnow.data.repository.reminder.ReminderRepository
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.util.DomainUtil.checkIfActive
import com.example.inhabitnow.domain.util.DomainUtil.checkIfScheduled
import com.example.inhabitnow.domain.util.DomainUtil.checkIfTaskScheduled
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class DefaultSetUpNextReminderUseCase(
    private val reminderRepository: ReminderRepository,
    private val taskRepository: TaskRepository,
    private val reminderManager: ReminderManager,
    private val defaultDispatcher: CoroutineDispatcher
) : SetUpNextReminderUseCase {

    override suspend operator fun invoke(reminderId: String) {
        withContext(defaultDispatcher) {
            reminderRepository.readReminderById(reminderId).firstOrNull()?.let { reminderEntity ->
                taskRepository.readTaskWithContentById(reminderEntity.taskId).firstOrNull()
                    ?.let { taskWithContentEntity ->
                        if (taskWithContentEntity.checkIfActive()) {
                            if (reminderEntity.type in setOf(ReminderType.Notification)) {
                                calculateNextReminderDateTime(
                                    taskWithContentEntity,
                                    reminderEntity
                                )?.let { targetDateTime ->
                                    reminderManager.resetReminderById(reminderId)
                                    targetDateTime
                                        .toInstant(TimeZone.currentSystemDefault())
                                        .toEpochMilliseconds().let { epochMillis ->
                                            reminderManager.setReminder(
                                                reminderId = reminderId,
                                                epochMillis = epochMillis
                                            )
                                        }
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun calculateNextReminderDateTime(
        taskWithContentEntity: TaskWithContentEntity,
        reminderEntity: ReminderEntity
    ): LocalDateTime? {
        maxOf(taskWithContentEntity.task.startDate, nowDateTime.date).let { startDate ->
            (taskWithContentEntity.task.endDate ?: distantFutureDate).let { endDate ->
                startDate.daysUntil(endDate).let { daysUntilEnd ->
                    (0..daysUntilEnd).forEach { offset ->
                        startDate.plus(offset, DateTimeUnit.DAY).let { currentDate ->
                            taskWithContentEntity.frequencyContent.content.checkIfTaskScheduled(
                                currentDate
                            ).let { isTaskScheduled ->
                                if (isTaskScheduled) {
                                    reminderEntity.schedule.checkIfScheduled(currentDate)
                                        .let { isReminderScheduled ->
                                            if (isReminderScheduled) {
                                                LocalDateTime(
                                                    currentDate,
                                                    reminderEntity.time
                                                ).let { targetDateTime ->
                                                    if (targetDateTime > nowDateTime) {
                                                        return targetDateTime
                                                    }
                                                }
                                            }
                                        }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null
    }

    private val nowDateTime: LocalDateTime
        get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    private val distantFutureDate: LocalDate
        get() = nowDateTime.date.plus(1, DateTimeUnit.MONTH)

}