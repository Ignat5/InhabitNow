package com.example.inhabitnow.android.presentation.create_edit_task.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.model.ItemTaskConfig
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.PickTaskFrequencyStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.components.PickTaskFrequencyScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.PickTaskTitleStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.components.PickTaskTitleScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number.PickTaskNumberProgressStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number.components.PickTaskNumberProgressScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.time.PickTaskTimeProgressStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.time.components.PickTaskTimeProgressScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenConfig
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenNavigation
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenState
import com.example.inhabitnow.android.presentation.model.UITaskContent
import com.example.inhabitnow.android.ui.toFrequencyContent
import com.example.inhabitnow.android.ui.toUIDateContent
import com.example.inhabitnow.android.ui.toUIFrequencyContent
import com.example.inhabitnow.android.ui.toUIProgressContent
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.domain.model.task.TaskWithContentModel
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import com.example.inhabitnow.domain.use_case.read_task_with_content_by_id.ReadTaskWithContentByIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.read_reminders_count_by_task_id.ReadRemindersCountByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_frequency_by_id.UpdateTaskFrequencyByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_progress_by_id.UpdateTaskProgressByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_title_by_id.UpdateTaskTitleByIdUseCase
import com.example.inhabitnow.domain.util.DomainConst
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val readTaskWithContentByIdUseCase: ReadTaskWithContentByIdUseCase,
    private val readRemindersCountByTaskIdUseCase: ReadRemindersCountByTaskIdUseCase,
    private val updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase,
    private val updateTaskProgressByIdUseCase: UpdateTaskProgressByIdUseCase,
    private val updateTaskFrequencyByIdUseCase: UpdateTaskFrequencyByIdUseCase,
    @DefaultDispatcherQualifier private val defaultDispatcher: CoroutineDispatcher
) : BaseViewModel<CreateTaskScreenEvent, CreateTaskScreenState, CreateTaskScreenNavigation, CreateTaskScreenConfig>() {

    private val taskId: String = checkNotNull(savedStateHandle.get<String>(AppNavDest.TASK_ID_KEY))

    private val taskWithContentState = readTaskWithContentByIdUseCase(taskId)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    private val taskRemindersCountState = readRemindersCountByTaskIdUseCase(taskId)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            0
        )

    override val uiScreenState: StateFlow<CreateTaskScreenState> =
        combine(
            taskWithContentState,
            taskRemindersCountState
        ) { taskWithContent, taskRemindersCount ->
            CreateTaskScreenState(
                allTaskConfigItems = provideTaskConfigItems(
                    taskWithContentModel = taskWithContent,
                    taskRemindersCount = taskRemindersCount
                ),
                canSave = taskWithContent?.task?.title?.isNotBlank() == true
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CreateTaskScreenState(
                allTaskConfigItems = emptyList(),
                canSave = false
            )
        )

    override fun onEvent(event: CreateTaskScreenEvent) {
        when (event) {
            is CreateTaskScreenEvent.OnItemTaskConfigClick -> onItemTaskConfigClick(event)
            is CreateTaskScreenEvent.ResultEvent -> onResultEvent(event)
            is CreateTaskScreenEvent.OnDismissRequest -> onDismissRequest()
            else -> Unit
        }
    }

    private fun onItemTaskConfigClick(event: CreateTaskScreenEvent.OnItemTaskConfigClick) {
        when (event.item) {
            is ItemTaskConfig.Title -> onConfigTaskTitleClick()
            is ItemTaskConfig.Progress -> {
                when (event.item) {
                    is ItemTaskConfig.Progress.Number -> onConfigTaskNumberProgressClick()
                    is ItemTaskConfig.Progress.Time -> onConfigTaskTimeProgressClick()
                }
            }

            is ItemTaskConfig.Frequency -> onConfigTaskFrequencyClick()
            is ItemTaskConfig.Reminders -> onConfigTaskRemindersClick()

            else -> Unit
        }
    }

    private fun onConfigTaskRemindersClick() {
        setUpNavigationState(CreateTaskScreenNavigation.ViewReminders(taskId))
    }

    private fun onConfigTaskFrequencyClick() {
        taskWithContentState.value?.frequencyContent?.toUIFrequencyContent()?.let { fc ->
            setUpConfigState(
                CreateTaskScreenConfig.PickTaskFrequency(
                    stateHolder = PickTaskFrequencyStateHolder(
                        initFrequency = fc,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onConfigTaskTimeProgressClick() {
        (taskWithContentState.value?.progressContent as? TaskContentModel.ProgressContent.Time)?.let { pc ->
            setUpConfigState(
                CreateTaskScreenConfig.PickTaskTimeProgress(
                    stateHolder = PickTaskTimeProgressStateHolder(
                        initProgressContent = pc,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onConfigTaskNumberProgressClick() {
        (taskWithContentState.value?.progressContent as? TaskContentModel.ProgressContent.Number)?.let { pc ->
            setUpConfigState(
                CreateTaskScreenConfig.PickTaskNumberProgress(
                    stateHolder = PickTaskNumberProgressStateHolder(
                        initProgressContent = pc,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onConfigTaskTitleClick() {
        taskWithContentState.value?.task?.title?.let { title ->
            setUpConfigState(
                CreateTaskScreenConfig.PickTitle(
                    stateHolder = PickTaskTitleStateHolder(
                        initTitle = title,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onResultEvent(event: CreateTaskScreenEvent.ResultEvent) {
        when (event) {
            is CreateTaskScreenEvent.ResultEvent.PickTaskTitle ->
                onPickTaskTitleResult(event)

            is CreateTaskScreenEvent.ResultEvent.PickTaskNumberProgress ->
                onPickTaskNumberProgressResult(event)

            is CreateTaskScreenEvent.ResultEvent.PickTaskTimeProgress ->
                onPickTaskTimeProgressResult(event)

            is CreateTaskScreenEvent.ResultEvent.PickTaskFrequency ->
                onPickTaskFrequency(event)
        }
    }

    private fun onPickTaskFrequency(event: CreateTaskScreenEvent.ResultEvent.PickTaskFrequency) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskFrequencyScreenResult.Confirm -> onConfirmPickTaskFrequency(result)
                is PickTaskFrequencyScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskFrequency(result: PickTaskFrequencyScreenResult.Confirm) {
        viewModelScope.launch {
            updateTaskFrequencyByIdUseCase(
                taskId = taskId,
                content = result.uiFrequencyContent.toFrequencyContent()
            )
        }
    }

    private fun onPickTaskTimeProgressResult(event: CreateTaskScreenEvent.ResultEvent.PickTaskTimeProgress) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskTimeProgressScreenResult.Confirm -> onConfirmPickTaskTimeProgress(result)
                is PickTaskTimeProgressScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskTimeProgress(result: PickTaskTimeProgressScreenResult.Confirm) {
        viewModelScope.launch {
            updateTaskProgressByIdUseCase(
                taskId = taskId,
                progressContent = result.progressContent
            )
        }
    }

    private fun onPickTaskNumberProgressResult(event: CreateTaskScreenEvent.ResultEvent.PickTaskNumberProgress) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskNumberProgressScreenResult.Confirm ->
                    onConfirmPickTaskNumberProgressResult(result)

                is PickTaskNumberProgressScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskNumberProgressResult(result: PickTaskNumberProgressScreenResult.Confirm) {
        viewModelScope.launch {
            updateTaskProgressByIdUseCase(
                taskId = taskId,
                progressContent = result.progressContent
            )
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

    private suspend fun provideTaskConfigItems(
        taskWithContentModel: TaskWithContentModel?,
        taskRemindersCount: Int
    ): List<ItemTaskConfig> =
        withContext(defaultDispatcher) {
            if (taskWithContentModel != null) {
                mutableListOf<ItemTaskConfig>().apply {
                    add(ItemTaskConfig.Title(taskWithContentModel.task.title))
                    add(ItemTaskConfig.Description(taskWithContentModel.task.description))
                    when (val pc = taskWithContentModel.progressContent.toUIProgressContent()) {
                        is UITaskContent.Progress.Number -> {
                            add(
                                ItemTaskConfig.Progress.Number(pc)
                            )
                        }

                        is UITaskContent.Progress.Time -> {
                            add(
                                ItemTaskConfig.Progress.Time(pc)
                            )
                        }

                        else -> Unit
                    }

                    when (val fc = taskWithContentModel.frequencyContent.toUIFrequencyContent()) {
                        is UITaskContent.Frequency -> {
                            add(
                                ItemTaskConfig.Frequency(fc)
                            )
                        }

                        else -> Unit
                    }

                    when (val dc = taskWithContentModel.task.toUIDateContent()) {
                        is UITaskContent.Date.OneDay -> add(
                            ItemTaskConfig.Date.OneDayDate(dc.date)
                        )

                        is UITaskContent.Date.Period -> {
                            add(ItemTaskConfig.Date.StartDate(dc.startDate))
                            add(ItemTaskConfig.Date.EndDate(dc.endDate))
                        }
                    }

                    add(ItemTaskConfig.Reminders(taskRemindersCount))
                    add(ItemTaskConfig.Tags(0))
                    add(ItemTaskConfig.Priority(taskWithContentModel.task.priority))

                }.sortedBy { it.key.ordinal }
            } else emptyList()
        }
}