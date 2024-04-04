package com.example.inhabitnow.android.presentation.create_edit_task.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.presentation.create_edit_task.base.BaseCreateEditTaskViewModel
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenConfig
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.confirm_leave.ConfirmLeaveScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenConfig
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenNavigation
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenState
import com.example.inhabitnow.domain.use_case.read_task_with_content_by_id.ReadTaskWithContentByIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.read_reminders_count_by_task_id.ReadRemindersCountByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.save_task_by_id.SaveTaskByIdUseCase
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
class CreateTaskViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
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
    private val saveTaskByIdUseCase: SaveTaskByIdUseCase,
    @DefaultDispatcherQualifier private val defaultDispatcher: CoroutineDispatcher,
) : BaseCreateEditTaskViewModel<CreateTaskScreenEvent, CreateTaskScreenState, CreateTaskScreenNavigation, CreateTaskScreenConfig>(
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

    private val canSaveState = taskModelState.map { taskModel ->
        taskModel?.title?.isNotBlank() == true
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false
    )

    override val uiScreenState: StateFlow<CreateTaskScreenState> =
        combine(allTaskConfigItemsState, canSaveState) { allTaskConfigItems, canSave ->
            CreateTaskScreenState(
                allTaskConfigItems = allTaskConfigItems,
                canSave = canSave
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CreateTaskScreenState(
                allTaskConfigItems = allTaskConfigItemsState.value,
                canSave = canSaveState.value
            )
        )

    override fun onEvent(event: CreateTaskScreenEvent) {
        when (event) {
            is CreateTaskScreenEvent.Base -> onBaseEvent(event.baseEvent)
            is CreateTaskScreenEvent.ResultEvent -> onResultEvent(event)
            is CreateTaskScreenEvent.OnSaveClick -> onSaveClick()
            is CreateTaskScreenEvent.OnDismissRequest -> onDismissRequest()
        }
    }

    private fun onResultEvent(event: CreateTaskScreenEvent.ResultEvent) {
        when (event) {
            is CreateTaskScreenEvent.ResultEvent.ConfirmLeave ->
                onConfirmLeaveResultEvent(event)
        }
    }

    private fun onConfirmLeaveResultEvent(event: CreateTaskScreenEvent.ResultEvent.ConfirmLeave) {
        onIdleToAction {
            when (event.result) {
                is ConfirmLeaveScreenResult.Confirm -> onConfirmLeave()
                is ConfirmLeaveScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmLeave() {
        setUpNavigationState(CreateTaskScreenNavigation.Back)
    }

    private fun onSaveClick() {
        if (canSaveState.value) {
            viewModelScope.launch {
                saveTaskByIdUseCase(taskId)
                setUpNavigationState(CreateTaskScreenNavigation.Back)
            }
        }
    }

    private fun onDismissRequest() {
        if (canSaveState.value) {
            setUpConfigState(CreateTaskScreenConfig.ConfirmLeave)
        } else {
            setUpNavigationState(CreateTaskScreenNavigation.Back)
        }
    }

    override fun setUpBaseConfigState(baseConfig: BaseCreateEditTaskScreenConfig) {
        setUpConfigState(CreateTaskScreenConfig.Base(baseConfig))
    }

    override fun setUpBaseNavigationState(baseSN: BaseCreateEditTaskScreenNavigation) {
        setUpNavigationState(CreateTaskScreenNavigation.Base(baseSN))
    }

}