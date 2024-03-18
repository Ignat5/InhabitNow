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
import com.example.inhabitnow.domain.use_case.reminder.read_reminders_by_task_id.ReadRemindersByTaskIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ViewTaskRemindersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val readRemindersByTaskIdUseCase: ReadRemindersByTaskIdUseCase
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
            is ViewTaskRemindersScreenEvent.OnReminderClick -> onReminderClick(event)
            is ViewTaskRemindersScreenEvent.OnCreateReminderClick -> onCreateReminderClick()
            is ViewTaskRemindersScreenEvent.OnDeleteReminderClick -> onDeleteReminderClick(event)
            is ViewTaskRemindersScreenEvent.OnBackClick -> onBackClick()
        }
    }

    private fun onReminderClick(event: ViewTaskRemindersScreenEvent.OnReminderClick) {

    }

    private fun onCreateReminderClick() {

    }

    private fun onDeleteReminderClick(event: ViewTaskRemindersScreenEvent.OnDeleteReminderClick) {

    }

    private fun onBackClick() {
        setUpNavigationState(ViewTaskRemindersScreenNavigation.Back)
    }

}