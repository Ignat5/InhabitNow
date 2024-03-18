package com.example.inhabitnow.android.presentation.view_task_reminders

import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.view_task_reminders.components.ViewTaskRemindersScreenConfig
import com.example.inhabitnow.android.presentation.view_task_reminders.components.ViewTaskRemindersScreenEvent
import com.example.inhabitnow.android.presentation.view_task_reminders.components.ViewTaskRemindersScreenNavigation
import com.example.inhabitnow.android.presentation.view_task_reminders.components.ViewTaskRemindersScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ViewTaskRemindersViewModel @Inject constructor(

) : BaseViewModel<ViewTaskRemindersScreenEvent, ViewTaskRemindersScreenState, ViewTaskRemindersScreenNavigation, ViewTaskRemindersScreenConfig>() {

    override val uiScreenState: StateFlow<ViewTaskRemindersScreenState>
        get() = TODO("Not yet implemented")

    override fun onEvent(event: ViewTaskRemindersScreenEvent) {
        TODO("Not yet implemented")
    }

}