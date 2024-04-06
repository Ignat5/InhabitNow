package com.example.inhabitnow.domain.use_case.reminder.delete_reminder_by_id

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.core.platform.ReminderManager
import com.example.inhabitnow.data.repository.reminder.ReminderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DefaultDeleteReminderByIdUseCase(
    private val reminderRepository: ReminderRepository,
    private val reminderManager: ReminderManager,
    private val externalScope: CoroutineScope
) : DeleteReminderByIdUseCase {

    override suspend operator fun invoke(reminderId: String): ResultModel<Unit> {
        val resultModel = reminderRepository.deleteReminderById(reminderId)
        if (resultModel is ResultModel.Success) {
            externalScope.launch {
                reminderManager.resetReminderById(reminderId)
            }
        }
        return resultModel
    }

}