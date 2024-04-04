package com.example.inhabitnow.domain.use_case.reminder.save_reminder

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.core.model.ResultModelWithException
import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.core.util.randomUUID
import com.example.inhabitnow.data.model.reminder.ReminderEntity
import com.example.inhabitnow.data.repository.reminder.ReminderRepository
import com.example.inhabitnow.domain.model.exceptions.SaveReminderException
import com.example.inhabitnow.domain.model.reminder.content.ReminderContentModel
import com.example.inhabitnow.domain.util.DomainUtil
import com.example.inhabitnow.domain.util.toScheduleModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime

class DefaultSaveReminderUseCase(
    private val reminderRepository: ReminderRepository,
    private val defaultDispatcher: CoroutineDispatcher,
    private val externalScope: CoroutineScope
) : SaveReminderUseCase {

    override suspend operator fun invoke(
        taskId: String,
        reminderType: ReminderType,
        reminderTime: LocalTime,
        reminderSchedule: ReminderContentModel.ScheduleContent
    ): ResultModelWithException<Unit, SaveReminderException> = withContext(defaultDispatcher) {
        ReminderEntity(
            id = randomUUID(),
            taskId = taskId,
            type = reminderType,
            time = reminderTime,
            schedule = reminderSchedule.toScheduleModel(),
            createdAt = Clock.System.now().toEpochMilliseconds()
        ).let { reminderEntity ->
            val isOverlaps = checkIfReminderOverlaps(reminderEntity)
            if (!isOverlaps) {
                val resultModel = reminderRepository.saveReminder(reminderEntity)
                if (resultModel is ResultModel.Success) {
                    externalScope.launch {
                        // set up reminder
                    }
                }
                when (resultModel) {
                    is ResultModel.Success -> ResultModelWithException.Success(Unit)
                    is ResultModel.Error -> ResultModelWithException.Error(
                        SaveReminderException.Other(resultModel.throwable)
                    )
                }
            } else {
                ResultModelWithException.Error(SaveReminderException.ScheduleOverlap)
            }
        }
    }

    private suspend fun checkIfReminderOverlaps(reminderEntity: ReminderEntity) =
        reminderRepository.readRemindersByTaskId(reminderEntity.taskId).firstOrNull()
            ?.let { allReminders ->
                allReminders.any { nextReminder ->
                    nextReminder.time == reminderEntity.time && DomainUtil.checkIfRemindersOverlap(
                        sourceSchedule = nextReminder.schedule,
                        targetSchedule = reminderEntity.schedule
                    )
                }
            } ?: false


}