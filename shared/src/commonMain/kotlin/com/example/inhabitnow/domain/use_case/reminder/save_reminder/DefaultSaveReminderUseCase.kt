package com.example.inhabitnow.domain.use_case.reminder.save_reminder

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.core.model.ResultModelWithException
import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.data.model.reminder.content.ReminderContentEntity
import com.example.inhabitnow.data.repository.reminder.ReminderRepository
import com.example.inhabitnow.domain.model.exceptions.SaveReminderException
import com.example.inhabitnow.domain.model.reminder.content.ReminderContentModel
import com.example.inhabitnow.domain.use_case.reminder.set_up_next_reminder.SetUpNextReminderUseCase
import com.example.inhabitnow.domain.util.DomainUtil
import com.example.inhabitnow.domain.util.toScheduleEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalTime

class DefaultSaveReminderUseCase(
    private val reminderRepository: ReminderRepository,
    private val setUpNextReminderUseCase: SetUpNextReminderUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
    private val externalScope: CoroutineScope
) : SaveReminderUseCase {

    override suspend operator fun invoke(
        taskId: String,
        reminderType: ReminderType,
        reminderTime: LocalTime,
        reminderSchedule: ReminderContentModel.ScheduleContent
    ): ResultModelWithException<Unit, SaveReminderException> = withContext(defaultDispatcher) {
        val doesOverlap = checkIfReminderOverlaps(
            taskId = taskId,
            reminderTime = reminderTime,
            reminderSchedule = reminderSchedule.toScheduleEntity()
        )
        if (!doesOverlap) {
            val resultModel = reminderRepository.saveReminder(
                taskId = taskId,
                reminderType = reminderType,
                reminderTime = reminderTime,
                reminderSchedule = reminderSchedule.toScheduleEntity()
            )
            if (resultModel is ResultModel.Success) {
                externalScope.launch {
                    val reminderId = resultModel.data
                    setUpNextReminderUseCase(reminderId)
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

    private suspend fun checkIfReminderOverlaps(
        taskId: String,
        reminderTime: LocalTime,
        reminderSchedule: ReminderContentEntity.ScheduleContent
    ) = reminderRepository.readRemindersByTaskId(taskId).firstOrNull()
        ?.let { allReminders ->
            allReminders.any { nextReminder ->
                nextReminder.time == reminderTime && DomainUtil.checkIfRemindersOverlap(
                    sourceSchedule = nextReminder.schedule,
                    targetSchedule = reminderSchedule
                )
            }
        } ?: false


}