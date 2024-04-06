package com.example.inhabitnow.android.presentation.create_edit_task.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.presentation.create_edit_task.base.BaseCreateEditTaskViewModel
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenConfig
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation
import com.example.inhabitnow.android.presentation.create_edit_task.edit.components.EditTaskScreenConfig
import com.example.inhabitnow.android.presentation.create_edit_task.edit.components.EditTaskScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.edit.components.EditTaskScreenNavigation
import com.example.inhabitnow.android.presentation.create_edit_task.edit.components.EditTaskScreenState
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_archive.ConfirmArchiveTaskScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_delete.ConfirmDeleteTaskScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_restart.ConfirmRestartHabitScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.edit.model.ItemTaskAction
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.domain.model.task.TaskModel
import com.example.inhabitnow.domain.use_case.archive_task_by_id.ArchiveTaskByIdUseCase
import com.example.inhabitnow.domain.use_case.delete_task_by_id.DeleteTaskByIdUseCase
import com.example.inhabitnow.domain.use_case.read_task_with_content_by_id.ReadTaskWithContentByIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.read_reminders_count_by_task_id.ReadRemindersCountByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.restart_habit_by_id.RestartHabitByIdUseCase
import com.example.inhabitnow.domain.use_case.tag.read_tag_ids_by_task_id.ReadTagIdsByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.tag.read_tags.ReadTagsUseCase
import com.example.inhabitnow.domain.use_case.tag.save_tag_cross_by_task_id.SaveTagCrossByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_date.UpdateTaskDateUseCase
import com.example.inhabitnow.domain.use_case.update_task_description.UpdateTaskDescriptionByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_frequency_by_id.UpdateTaskFrequencyByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_priority_by_id.UpdateTaskPriorityByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_progress_by_id.UpdateTaskProgressByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_title_by_id.UpdateTaskTitleByIdUseCase
import com.example.inhabitnow.domain.use_case.validate_limit_number.ValidateInputLimitNumberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    readTaskWithContentByIdUseCase: ReadTaskWithContentByIdUseCase,
    readRemindersCountByTaskIdUseCase: ReadRemindersCountByTaskIdUseCase,
    readTagsUseCase: ReadTagsUseCase,
    readTagIdsByTaskIdUseCase: ReadTagIdsByTaskIdUseCase,
    updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase,
    updateTaskProgressByIdUseCase: UpdateTaskProgressByIdUseCase,
    updateTaskFrequencyByIdUseCase: UpdateTaskFrequencyByIdUseCase,
    updateTaskDateUseCase: UpdateTaskDateUseCase,
    updateTaskDescriptionByIdUseCase: UpdateTaskDescriptionByIdUseCase,
    updateTaskPriorityByIdUseCase: UpdateTaskPriorityByIdUseCase,
    saveTagCrossByTaskIdUseCase: SaveTagCrossByTaskIdUseCase,
    validateInputLimitNumberUseCase: ValidateInputLimitNumberUseCase,
    private val archiveTaskByIdUseCase: ArchiveTaskByIdUseCase,
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    private val restartHabitByIdUseCase: RestartHabitByIdUseCase,
    @DefaultDispatcherQualifier private val defaultDispatcher: CoroutineDispatcher,
) : BaseCreateEditTaskViewModel<EditTaskScreenEvent, EditTaskScreenState, EditTaskScreenNavigation, EditTaskScreenConfig>(
    taskId = checkNotNull(savedStateHandle.get<String>(AppNavDest.TASK_ID_KEY)),
    readTaskWithContentByIdUseCase = readTaskWithContentByIdUseCase,
    readRemindersCountByTaskIdUseCase = readRemindersCountByTaskIdUseCase,
    readTagsUseCase = readTagsUseCase,
    readTagIdsByTaskIdUseCase = readTagIdsByTaskIdUseCase,
    updateTaskTitleByIdUseCase = updateTaskTitleByIdUseCase,
    updateTaskDescriptionByIdUseCase = updateTaskDescriptionByIdUseCase,
    updateTaskPriorityByIdUseCase = updateTaskPriorityByIdUseCase,
    updateTaskProgressByIdUseCase = updateTaskProgressByIdUseCase,
    updateTaskFrequencyByIdUseCase = updateTaskFrequencyByIdUseCase,
    updateTaskDateUseCase = updateTaskDateUseCase,
    saveTagCrossByTaskIdUseCase = saveTagCrossByTaskIdUseCase,
    validateInputLimitNumberUseCase = validateInputLimitNumberUseCase,
    defaultDispatcher = defaultDispatcher
) {

    private val allTaskActionItemsState = taskModelState.map { taskModel ->
        if (taskModel != null) {
            mutableListOf<ItemTaskAction>().apply {
                if (taskModel is TaskModel.Habit) {
                    add(ItemTaskAction.ViewStatistics)
                    add(ItemTaskAction.RestartHabit)
                }
                add(
                    if (taskModel.isArchived) ItemTaskAction.ArchiveUnarchive.Unarchive
                    else ItemTaskAction.ArchiveUnarchive.Archive
                )
                add(ItemTaskAction.DeleteTask)
            }
        } else emptyList()
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    override val uiScreenState: StateFlow<EditTaskScreenState> =
        combine(
            allTaskConfigItemsState,
            allTaskActionItemsState
        ) { allTaskConfigItems, allTaskActionItems ->
            EditTaskScreenState(
                allTaskConfigItems = allTaskConfigItems,
                allTaskActionItems = allTaskActionItems
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            EditTaskScreenState(
                allTaskConfigItems = allTaskConfigItemsState.value,
                allTaskActionItems = allTaskActionItemsState.value
            )
        )

    override fun onEvent(event: EditTaskScreenEvent) {
        when (event) {
            is EditTaskScreenEvent.BaseEvent -> onBaseEvent(event.baseEvent)
            is EditTaskScreenEvent.OnItemTaskActionClick -> onItemTaskActionClick(event)
            is EditTaskScreenEvent.ResultEvent -> onResultEvent(event)
            is EditTaskScreenEvent.OnBackRequest -> onBackRequest()
        }
    }

    private fun onResultEvent(event: EditTaskScreenEvent.ResultEvent) {
        when (event) {
            is EditTaskScreenEvent.ResultEvent.ConfirmArchiveTask ->
                onConfirmArchiveTaskResultEvent(event)

            is EditTaskScreenEvent.ResultEvent.ConfirmDeleteTask ->
                onConfirmDeleteTask(event)

            is EditTaskScreenEvent.ResultEvent.ConfirmRestartHabit ->
                onConfirmRestartHabitResultEvent(event)
        }
    }

    private fun onConfirmRestartHabitResultEvent(event: EditTaskScreenEvent.ResultEvent.ConfirmRestartHabit) {
        onIdleToAction {
            when (event.result) {
                is ConfirmRestartHabitScreenResult.Confirm -> onConfirmRestartHabit()
                is ConfirmRestartHabitScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmRestartHabit() {
        viewModelScope.launch {
            restartHabitByIdUseCase(taskId)
        }
    }

    private fun onConfirmDeleteTask(event: EditTaskScreenEvent.ResultEvent.ConfirmDeleteTask) {
        onIdleToAction {
            when (val result = event.result) {
                is ConfirmDeleteTaskScreenResult.Confirm -> onConfirmDeleteTask()
                is ConfirmDeleteTaskScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmDeleteTask() {
        viewModelScope.launch {
            deleteTaskByIdUseCase(taskId)
            setUpNavigationState(EditTaskScreenNavigation.Back)
        }
    }

    private fun onConfirmArchiveTaskResultEvent(event: EditTaskScreenEvent.ResultEvent.ConfirmArchiveTask) {
        onIdleToAction {
            when (event.result) {
                is ConfirmArchiveTaskScreenResult.Confirm -> onConfirmArchiveTask()
                is ConfirmArchiveTaskScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmArchiveTask() {
        viewModelScope.launch {
            archiveTaskByIdUseCase(
                taskId = taskId,
                archive = true
            )
        }
    }

    private fun onItemTaskActionClick(event: EditTaskScreenEvent.OnItemTaskActionClick) {
        when (val item = event.item) {
            is ItemTaskAction.ViewStatistics -> onViewStatistics()
            is ItemTaskAction.RestartHabit -> onRestartHabit()
            is ItemTaskAction.ArchiveUnarchive -> {
                when (item) {
                    is ItemTaskAction.ArchiveUnarchive.Archive -> onArchiveTask()
                    is ItemTaskAction.ArchiveUnarchive.Unarchive -> onUnarchiveTask()
                }
            }

            is ItemTaskAction.DeleteTask -> onDeleteTask()
        }
    }

    private fun onArchiveTask() {
        setUpConfigState(EditTaskScreenConfig.ConfirmArchiveTask(taskId))
    }

    private fun onUnarchiveTask() {
        viewModelScope.launch {
            archiveTaskByIdUseCase(
                taskId = taskId,
                archive = false
            )
        }
    }

    private fun onRestartHabit() {
        setUpConfigState(EditTaskScreenConfig.ConfirmRestartHabit(taskId))
    }

    private fun onViewStatistics() {
        setUpNavigationState(EditTaskScreenNavigation.ViewStatistics(taskId))
    }

    private fun onDeleteTask() {
        setUpConfigState(EditTaskScreenConfig.ConfirmDeleteTask(taskId))
    }

    private fun onBackRequest() {
        setUpNavigationState(EditTaskScreenNavigation.Back)
    }

    override fun setUpBaseConfigState(baseConfig: BaseCreateEditTaskScreenConfig) {
        setUpConfigState(EditTaskScreenConfig.BaseConfig(baseConfig))
    }

    override fun setUpBaseNavigationState(baseSN: BaseCreateEditTaskScreenNavigation) {
        setUpNavigationState(EditTaskScreenNavigation.Base(baseSN))
    }

}