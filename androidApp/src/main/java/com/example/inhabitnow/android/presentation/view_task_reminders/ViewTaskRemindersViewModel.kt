package com.example.inhabitnow.android.presentation.view_task_reminders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.android.presentation.view_task_reminders.components.ViewTaskRemindersScreenConfig
import com.example.inhabitnow.android.presentation.view_task_reminders.components.ViewTaskRemindersScreenEvent
import com.example.inhabitnow.android.presentation.view_task_reminders.components.ViewTaskRemindersScreenNavigation
import com.example.inhabitnow.android.presentation.view_task_reminders.components.ViewTaskRemindersScreenState
import com.example.inhabitnow.android.presentation.view_task_reminders.config.confirm_delete_reminder.ConfirmDeleteReminderScreenResult
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.create.CreateReminderStateHolder
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.create.components.CreateReminderScreenResult
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.edit.EditReminderStateHolder
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.edit.components.EditReminderScreenResult
import com.example.inhabitnow.android.ui.toScheduleContent
import com.example.inhabitnow.android.ui.toUIScheduleContent
import com.example.inhabitnow.domain.use_case.reminder.delete_reminder_by_id.DeleteReminderByIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.read_reminders_by_task_id.ReadRemindersByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.save_reminder.SaveReminderUseCase
import com.example.inhabitnow.domain.use_case.reminder.update_reminder.UpdateReminderByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewTaskRemindersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val readRemindersByTaskIdUseCase: ReadRemindersByTaskIdUseCase,
    private val saveReminderUseCase: SaveReminderUseCase,
    private val updateReminderByIdUseCase: UpdateReminderByIdUseCase,
    private val deleteReminderByIdUseCase: DeleteReminderByIdUseCase
) : BaseViewModel<ViewTaskRemindersScreenEvent, ViewTaskRemindersScreenState, ViewTaskRemindersScreenNavigation, ViewTaskRemindersScreenConfig>() {

    private val taskId: String = checkNotNull(savedStateHandle.get<String>(AppNavDest.TASK_ID_KEY))

    private val allRemindersState = readRemindersByTaskIdUseCase(taskId)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    override val uiScreenState: StateFlow<ViewTaskRemindersScreenState> =
        allRemindersState.map { allReminders ->
            ViewTaskRemindersScreenState(
                allRemindersResultModel = if (allReminders.isEmpty()) UIResultModel.NoData
                else UIResultModel.Data(allReminders)
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ViewTaskRemindersScreenState(
                allRemindersResultModel = UIResultModel.Loading(allRemindersState.value)
            )
        )

    override fun onEvent(event: ViewTaskRemindersScreenEvent) {
        when (event) {
            is ViewTaskRemindersScreenEvent.ResultEvent -> onResultEvent(event)
            is ViewTaskRemindersScreenEvent.OnReminderClick -> onReminderClick(event)
            is ViewTaskRemindersScreenEvent.OnCreateReminderClick -> onCreateReminderClick()
            is ViewTaskRemindersScreenEvent.OnDeleteReminderClick -> onDeleteReminderClick(event)
            is ViewTaskRemindersScreenEvent.OnBackClick -> onBackClick()
        }
    }

    private fun onResultEvent(event: ViewTaskRemindersScreenEvent.ResultEvent) {
        when (event) {
            is ViewTaskRemindersScreenEvent.ResultEvent.CreateReminder ->
                onCreateReminderResultEvent(event)

            is ViewTaskRemindersScreenEvent.ResultEvent.ConfirmDeleteReminder ->
                onConfirmDeleteReminderResultEvent(event)

            is ViewTaskRemindersScreenEvent.ResultEvent.EditReminder ->
                onEditReminderResultEvent(event)

        }
    }

    private fun onEditReminderResultEvent(event: ViewTaskRemindersScreenEvent.ResultEvent.EditReminder) {
        onIdleToAction {
            when (val result = event.result) {
                is EditReminderScreenResult.Confirm -> onConfirmEditReminder(result)
                is EditReminderScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmEditReminder(result: EditReminderScreenResult.Confirm) {
        allRemindersState.value.find { it.id == result.reminderId }?.let { reminderModel ->
            viewModelScope.launch {
                updateReminderByIdUseCase(
                    reminderModel = reminderModel.copy(
                        type = result.reminderType,
                        time = result.reminderTime,
                        schedule = result.reminderSchedule.toScheduleContent()
                    )
                )
            }
        }
    }

    private fun onConfirmDeleteReminderResultEvent(event: ViewTaskRemindersScreenEvent.ResultEvent.ConfirmDeleteReminder) {
        onIdleToAction {
            when (val result = event.result) {
                is ConfirmDeleteReminderScreenResult.Confirm -> onConfirmDeleteReminder(result)
                is ConfirmDeleteReminderScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmDeleteReminder(result: ConfirmDeleteReminderScreenResult.Confirm) {
        viewModelScope.launch {
            deleteReminderByIdUseCase(result.reminderId)
        }
    }

    private fun onCreateReminderResultEvent(event: ViewTaskRemindersScreenEvent.ResultEvent.CreateReminder) {
        onIdleToAction {
            when (val result = event.result) {
                is CreateReminderScreenResult.Confirm -> onConfirmCreateReminder(result)
                is CreateReminderScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmCreateReminder(result: CreateReminderScreenResult.Confirm) {
        viewModelScope.launch {
            saveReminderUseCase(
                taskId = taskId,
                reminderType = result.type,
                reminderTime = result.time,
                reminderSchedule = result.uiScheduleContent.toScheduleContent()
            )
        }
    }

    private fun onReminderClick(event: ViewTaskRemindersScreenEvent.OnReminderClick) {
        allRemindersState.value.find { it.id == event.reminderId }?.let { reminderModel ->
            setUpConfigState(
                ViewTaskRemindersScreenConfig.EditReminder(
                    stateHolder = EditReminderStateHolder(
                        reminderId = reminderModel.id,
                        reminderTime = reminderModel.time,
                        reminderType = reminderModel.type,
                        reminderSchedule = reminderModel.schedule.toUIScheduleContent(),
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onCreateReminderClick() {
        setUpConfigState(
            ViewTaskRemindersScreenConfig.CreateReminder(
                stateHolder = CreateReminderStateHolder(
                    holderScope = provideChildScope()
                )
            )
        )
    }

    private fun onDeleteReminderClick(event: ViewTaskRemindersScreenEvent.OnDeleteReminderClick) {
        setUpConfigState(ViewTaskRemindersScreenConfig.ConfirmDeleteReminder(event.reminderId))
    }

    private fun onBackClick() {
        setUpNavigationState(ViewTaskRemindersScreenNavigation.Back)
    }

}