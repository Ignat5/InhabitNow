package com.example.inhabitnow.android.presentation.create_edit_task.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.PickTaskTitleStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.components.PickTaskTitleScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenConfig
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenNavigation
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenState
import com.example.inhabitnow.android.presentation.model.UITaskContent
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.domain.model.task.TaskWithContentModel
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import com.example.inhabitnow.domain.use_case.read_task_with_content_by_id.ReadTaskWithContentByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_title_by_id.UpdateTaskTitleByIdUseCase
import com.example.inhabitnow.domain.util.DomainConst
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val readTaskWithContentByIdUseCase: ReadTaskWithContentByIdUseCase,
    private val updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase
) : BaseViewModel<CreateTaskScreenEvent, CreateTaskScreenState, CreateTaskScreenNavigation, CreateTaskScreenConfig>() {

    private val taskId: String = checkNotNull(savedStateHandle.get<String>(AppNavDest.TASK_ID_KEY))

    private val taskWithContentState = readTaskWithContentByIdUseCase(taskId)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    override val uiScreenState: StateFlow<CreateTaskScreenState> =
        taskWithContentState.map { taskWithContent ->
            provideScreenState(taskWithContent)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            provideScreenState(taskWithContentState.value)
        )

    override fun onEvent(event: CreateTaskScreenEvent) {
        when (event) {
            is CreateTaskScreenEvent.ConfigEvent -> onConfigEvent(event)
            is CreateTaskScreenEvent.ResultEvent -> onResultEvent(event)
            is CreateTaskScreenEvent.OnDismissRequest -> onDismissRequest()
            else -> Unit
        }
    }

    private fun onConfigEvent(event: CreateTaskScreenEvent.ConfigEvent) {
        when (event) {
            is CreateTaskScreenEvent.ConfigEvent.OnConfigTaskTitleClick ->
                onConfigTaskTitleClick()
        }
    }

    private fun onConfigTaskTitleClick() {
        taskWithContentState.value?.task?.title?.let { title ->
            setUpConfigState(
                CreateTaskScreenConfig.PickTitle(
                    stateHolder = PickTaskTitleStateHolder(
                        initTitle = title,
                        holderScope = viewModelScope
                    )
                )
            )
        }
    }

    private fun onResultEvent(event: CreateTaskScreenEvent.ResultEvent) {
        when (event) {
            is CreateTaskScreenEvent.ResultEvent.PickTaskTitle ->
                onPickTaskTitleResult(event)
        }
    }

    private fun onPickTaskTitleResult(event: CreateTaskScreenEvent.ResultEvent.PickTaskTitle) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskTitleScreenResult.Confirm -> onConfirmPickTaskTitleResult(result)
                is PickTaskTitleScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskTitleResult(result: PickTaskTitleScreenResult.Confirm) {
        viewModelScope.launch {
            updateTaskTitleByIdUseCase(
                taskId = taskId,
                title = result.input
            )
        }
    }

    private fun onDismissRequest() {
        setUpNavigationState(CreateTaskScreenNavigation.Back)
    }

    private fun provideScreenState(taskWithContentModel: TaskWithContentModel?): CreateTaskScreenState {
        return CreateTaskScreenState(
            taskTitle = taskWithContentModel?.task?.title ?: DomainConst.DEFAULT_TASK_TITLE,
            taskDescription = taskWithContentModel?.task?.description
                ?: DomainConst.DEFAULT_TASK_DESCRIPTION,
            taskPriority = taskWithContentModel?.task?.priority
                ?: DomainConst.DEFAULT_PRIORITY.toString(),
            taskProgressContent = taskWithContentModel?.progressContent?.toUIProgressContent(),
            taskFrequencyContent = taskWithContentModel?.frequencyContent?.toUIFrequencyContent(),
            taskDateContent = taskWithContentModel?.let {
                when (taskWithContentModel.task.type) {
                    TaskType.SingleTask -> UITaskContent.Date.OneDay(taskWithContentModel.task.startDate)
                    TaskType.RecurringTask, TaskType.Habit -> UITaskContent.Date.Period(
                        startDate = taskWithContentModel.task.startDate,
                        endDate = taskWithContentModel.task.endDate
                    )
                }
            },
            taskRemindersCount = 0,
            taskTagCount = 0,
            canSave = taskWithContentModel?.task?.title?.isNotBlank() == true
        )
    }

    private fun TaskContentModel.ProgressContent.toUIProgressContent(): UITaskContent.Progress? {
        return when (this) {
            is TaskContentModel.ProgressContent.Number -> UITaskContent.Progress.Number(this)
            is TaskContentModel.ProgressContent.Time -> UITaskContent.Progress.Time(this)
            else -> null
        }
    }

    private fun TaskContentModel.FrequencyContent.toUIFrequencyContent(): UITaskContent.Frequency? {
        return when (this) {
            is TaskContentModel.FrequencyContent.EveryDay -> UITaskContent.Frequency.EveryDay(this)
            is TaskContentModel.FrequencyContent.DaysOfWeek ->
                UITaskContent.Frequency.DaysOfWeek(this)

            else -> null
        }
    }

}