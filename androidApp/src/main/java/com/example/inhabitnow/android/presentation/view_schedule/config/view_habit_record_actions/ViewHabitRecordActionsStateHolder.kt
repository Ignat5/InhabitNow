package com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions

import com.example.inhabitnow.android.presentation.base.state_holder.BaseResultStateHolder
import com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions.components.ViewHabitRecordActionsScreenEvent
import com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions.components.ViewHabitRecordActionsScreenResult
import com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions.components.ViewHabitRecordActionsScreenState
import com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions.model.ItemHabitRecordAction
import com.example.inhabitnow.android.presentation.view_schedule.model.TaskWithRecordModel
import com.example.inhabitnow.domain.model.record.content.RecordContentModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate

class ViewHabitRecordActionsStateHolder(
    private val taskWithRecord: TaskWithRecordModel.Habit,
    private val date: LocalDate,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<ViewHabitRecordActionsScreenEvent, ViewHabitRecordActionsScreenState, ViewHabitRecordActionsScreenResult>() {

    private val allActionItems: List<ItemHabitRecordAction> =
        mutableListOf<ItemHabitRecordAction>().apply {
            when (taskWithRecord) {
                is TaskWithRecordModel.Habit.HabitContinuous -> {
                    when (taskWithRecord) {
                        is TaskWithRecordModel.Habit.HabitContinuous.HabitNumber -> {
                            add(
                                ItemHabitRecordAction.ContinuousProgress.Number(taskWithRecord)
                            )
                        }

                        is TaskWithRecordModel.Habit.HabitContinuous.HabitTime -> {
                            add(
                                ItemHabitRecordAction.ContinuousProgress.Time(taskWithRecord)
                            )
                        }
                    }
                }

                is TaskWithRecordModel.Habit.HabitYesNo -> {
                    if (taskWithRecord.recordEntry !is RecordContentModel.Entry.Done) {
                        add(ItemHabitRecordAction.Done)
                    }
                }
            }
            if (taskWithRecord.recordEntry !is RecordContentModel.Entry.Fail) {
                add(ItemHabitRecordAction.Fail)
            }
            if (taskWithRecord.recordEntry !is RecordContentModel.Entry.Skip) {
                add(ItemHabitRecordAction.Skip)
            }
            if (taskWithRecord.recordEntry != null) {
                add(ItemHabitRecordAction.Reset)
            }
        }

    override val uiScreenState: StateFlow<ViewHabitRecordActionsScreenState> =
        flow {
            emit(
                ViewHabitRecordActionsScreenState(
                    allActionItems = allActionItems,
                    taskWithRecord = taskWithRecord,
                    date = date
                )
            )
        }.stateIn(
            holderScope,
            SharingStarted.Eagerly,
            ViewHabitRecordActionsScreenState(
                allActionItems = allActionItems,
                taskWithRecord = taskWithRecord,
                date = date
            )
        )

    override fun onEvent(event: ViewHabitRecordActionsScreenEvent) {
        when (event) {
            is ViewHabitRecordActionsScreenEvent.OnActionClick ->
                onActionClick(event)

            is ViewHabitRecordActionsScreenEvent.OnEditClick ->
                onEditClick()

            is ViewHabitRecordActionsScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onActionClick(event: ViewHabitRecordActionsScreenEvent.OnActionClick) {
        setUpResult(
            ViewHabitRecordActionsScreenResult.Confirm(
                taskId = taskWithRecord.task.id,
                date = date,
                action = when (event.action) {
                    is ItemHabitRecordAction.ContinuousProgress -> ViewHabitRecordActionsScreenResult.Action.EnterRecord
                    is ItemHabitRecordAction.Done -> ViewHabitRecordActionsScreenResult.Action.EnterRecord
                    is ItemHabitRecordAction.Fail -> ViewHabitRecordActionsScreenResult.Action.Fail
                    is ItemHabitRecordAction.Skip -> ViewHabitRecordActionsScreenResult.Action.Skip
                    is ItemHabitRecordAction.Reset -> ViewHabitRecordActionsScreenResult.Action.ResetEntry
                }
            )
        )
    }

    private fun onEditClick() {
        setUpResult(
            ViewHabitRecordActionsScreenResult.Confirm(
                taskId = taskWithRecord.task.id,
                date = date,
                action = ViewHabitRecordActionsScreenResult.Action.Edit
            )
        )
    }

    private fun onDismissRequest() {
        setUpResult(ViewHabitRecordActionsScreenResult.Dismiss)
    }

}