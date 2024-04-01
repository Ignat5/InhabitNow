package com.example.inhabitnow.android.presentation.view_activities.view_habits.config.view_habit_actions

import com.example.inhabitnow.android.presentation.base.state_holder.BaseResultStateHolder
import com.example.inhabitnow.android.presentation.view_activities.model.ItemTaskAction
import com.example.inhabitnow.android.presentation.view_activities.view_habits.config.view_habit_actions.components.ViewHabitActionsScreenEvent
import com.example.inhabitnow.android.presentation.view_activities.view_habits.config.view_habit_actions.components.ViewHabitActionsScreenResult
import com.example.inhabitnow.android.presentation.view_activities.view_habits.config.view_habit_actions.components.ViewHabitActionsScreenState
import com.example.inhabitnow.domain.model.task.TaskModel
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class ViewHabitActionsStateHolder(
    private val habitModel: TaskModel.Habit,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<ViewHabitActionsScreenEvent, ViewHabitActionsScreenState, ViewHabitActionsScreenResult>() {

    private val allHabitActions: List<ItemTaskAction.HabitAction> = listOf(
        ItemTaskAction.ViewStatistics,
        (if (habitModel.isArchived) ItemTaskAction.ArchiveUnarchive.Unarchive
        else ItemTaskAction.ArchiveUnarchive.Archive),
        ItemTaskAction.Delete
    )

    override val uiScreenState: StateFlow<ViewHabitActionsScreenState> = MutableStateFlow(
        ViewHabitActionsScreenState(
            habitModel = habitModel,
            allActionItems = allHabitActions
        )
    )

    override fun onEvent(event: ViewHabitActionsScreenEvent) {
        when (event) {
            is ViewHabitActionsScreenEvent.OnItemActionClick ->
                onItemActionClick(event)

            is ViewHabitActionsScreenEvent.OnEditClick ->
                onEditClick()

            is ViewHabitActionsScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onItemActionClick(event: ViewHabitActionsScreenEvent.OnItemActionClick) {
        setUpResult(
            ViewHabitActionsScreenResult.Action(
                taskId = habitModel.id,
                action = event.itemTaskAction
            )
        )
    }

    private fun onEditClick() {
        setUpResult(
            ViewHabitActionsScreenResult.Edit(
                taskId = habitModel.id
            )
        )
    }

    private fun onDismissRequest() {
        setUpResult(ViewHabitActionsScreenResult.Dismiss)
    }

}