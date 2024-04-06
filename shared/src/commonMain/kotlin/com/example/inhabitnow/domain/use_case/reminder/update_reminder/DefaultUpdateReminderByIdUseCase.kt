package com.example.inhabitnow.domain.use_case.reminder.update_reminder

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.core.model.ResultModelWithException
import com.example.inhabitnow.data.model.reminder.content.ReminderContentEntity
import com.example.inhabitnow.data.repository.reminder.ReminderRepository
import com.example.inhabitnow.domain.model.exceptions.SaveReminderException
import com.example.inhabitnow.domain.model.reminder.ReminderModel
import com.example.inhabitnow.domain.use_case.reminder.set_up_next_reminder.SetUpNextReminderUseCase
import com.example.inhabitnow.domain.util.DomainUtil
import com.example.inhabitnow.domain.util.toScheduleEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalTime

class DefaultUpdateReminderByIdUseCase(
    private val reminderRepository: ReminderRepository,
    private val setUpNextReminderUseCase: SetUpNextReminderUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
    private val externalScope: CoroutineScope
) : UpdateReminderByIdUseCase {

    override suspend operator fun invoke(reminderModel: ReminderModel): ResultModelWithException<Unit, SaveReminderException> =
        withContext(defaultDispatcher) {
            val doesOverlap = checkReminderOverlap(
                reminderId = reminderModel.id,
                reminderTaskId = reminderModel.taskId,
                reminderTime = reminderModel.time,
                reminderSchedule = reminderModel.schedule.toScheduleEntity()
            )
            if (!doesOverlap) {
                val resultModel = reminderRepository.updateReminderById(
                    reminderId = reminderModel.id,
                    reminderTime = reminderModel.time,
                    reminderType = reminderModel.type,
                    reminderSchedule = reminderModel.schedule.toScheduleEntity()
                )
                if (resultModel is ResultModel.Success) {
                    externalScope.launch {
                        setUpNextReminderUseCase(reminderModel.id)
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

    private suspend fun checkReminderOverlap(
        reminderId: String,
        reminderTaskId: String,
        reminderTime: LocalTime,
        reminderSchedule: ReminderContentEntity.ScheduleContent
    ): Boolean = reminderRepository.readRemindersByTaskId(reminderTaskId).firstOrNull()
        ?.let { allReminders ->
            allReminders
                .filter { it.id != reminderId }
                .any { nextReminder ->
                    nextReminder.time == reminderTime && DomainUtil.checkIfRemindersOverlap(
                        sourceSchedule = nextReminder.schedule,
                        targetSchedule = reminderSchedule
                    )
                }
        } ?: false


}