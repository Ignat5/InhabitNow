package com.example.inhabitnow.android.presentation.create_edit_task.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.common.pick_date.PickDateStateHolder
import com.example.inhabitnow.android.presentation.common.pick_date.components.PickDateScreenResult
import com.example.inhabitnow.android.presentation.common.pick_date.model.PickDateRequestModel
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.confirm_leave.ConfirmLeaveScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.model.ItemTaskConfig
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description.PickTaskDescriptionStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description.components.PickTaskDescriptionScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.PickTaskFrequencyStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.components.PickTaskFrequencyScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_priority.PickTaskPriorityStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_priority.components.PickTaskPriorityScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.PickTaskTitleStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.components.PickTaskTitleScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number.PickTaskNumberProgressStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number.components.PickTaskNumberProgressScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.time.PickTaskTimeProgressStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.time.components.PickTaskTimeProgressScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.PickTaskTagsStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.components.PickTaskTagsScreenResult
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
import com.example.inhabitnow.domain.use_case.tag.read_tag_ids_by_task_id.ReadTagIdsByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.tag.read_tags.ReadTagsUseCase
import com.example.inhabitnow.domain.use_case.tag.save_tag_cross_by_task_id.SaveTagCrossByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_date.UpdateTaskDateUseCase
import com.example.inhabitnow.domain.use_case.update_task_description.UpdateTaskDescriptionByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_frequency_by_id.UpdateTaskFrequencyByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_priority_by_id.UpdateTaskPriorityByIdUseCase
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
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val readTaskWithContentByIdUseCase: ReadTaskWithContentByIdUseCase,
    private val readRemindersCountByTaskIdUseCase: ReadRemindersCountByTaskIdUseCase,
    private val readTagsUseCase: ReadTagsUseCase,
    private val readTagIdsByTaskIdUseCase: ReadTagIdsByTaskIdUseCase,
    private val updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase,
    private val updateTaskProgressByIdUseCase: UpdateTaskProgressByIdUseCase,
    private val updateTaskFrequencyByIdUseCase: UpdateTaskFrequencyByIdUseCase,
    private val updateTaskDateUseCase: UpdateTaskDateUseCase,
    private val updateTaskDescriptionByIdUseCase: UpdateTaskDescriptionByIdUseCase,
    private val updateTaskPriorityByIdUseCase: UpdateTaskPriorityByIdUseCase,
    private val saveTagCrossByTaskIdUseCase: SaveTagCrossByTaskIdUseCase,
    @DefaultDispatcherQualifier private val defaultDispatcher: CoroutineDispatcher
) : BaseViewModel<CreateTaskScreenEvent, CreateTaskScreenState, CreateTaskScreenNavigation, CreateTaskScreenConfig>() {

    private val taskId: String = checkNotNull(savedStateHandle.get<String>(AppNavDest.TASK_ID_KEY))
    private val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

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
            DEFAULT_REMINDER_COUNT
        )

    private val allTagsState = readTagsUseCase()
        .map { it.sortedBy { tag -> tag.createdAt } }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    private val taskTagIdsState: StateFlow<Set<String>> = readTagIdsByTaskIdUseCase(taskId)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptySet()
        )

    private val taskTagsCountState = taskTagIdsState
        .map { it.size }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            DEFAULT_TAG_COUNT
        )

    override val uiScreenState: StateFlow<CreateTaskScreenState> =
        combine(
            taskWithContentState,
            taskRemindersCountState,
            taskTagsCountState
        ) { taskWithContent, taskRemindersCount, taskTagsCount ->
            CreateTaskScreenState(
                allTaskConfigItems = provideTaskConfigItems(
                    taskWithContentModel = taskWithContent,
                    taskRemindersCount = taskRemindersCount,
                    taskTagsCount = taskTagsCount
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
            is CreateTaskScreenEvent.OnEndDateSwitchClick -> onEndDateSwitchClick()
            is CreateTaskScreenEvent.OnSaveClick -> {
                /* TODO */
            }

            is CreateTaskScreenEvent.OnDismissRequest -> onDismissRequest()
        }
    }

    private fun onEndDateSwitchClick() {
        taskWithContentState.value?.task?.let { task ->
            viewModelScope.launch {
                updateTaskDateUseCase(
                    taskId = taskId,
                    requestBody = UpdateTaskDateUseCase.RequestBody.EndDate(
                        date = if (task.endDate != null) null
                        else task.startDate.plus(1, DateTimeUnit.MONTH)
                    )
                )
            }
        }
    }

    private fun onItemTaskConfigClick(event: CreateTaskScreenEvent.OnItemTaskConfigClick) {
        when (event.item) {
            is ItemTaskConfig.Title -> onConfigTaskTitleClick()
            is ItemTaskConfig.Date -> onConfigDateClick(event.item)
            is ItemTaskConfig.Progress -> {
                when (event.item) {
                    is ItemTaskConfig.Progress.Number -> onConfigTaskNumberProgressClick()
                    is ItemTaskConfig.Progress.Time -> onConfigTaskTimeProgressClick()
                }
            }

            is ItemTaskConfig.Frequency -> onConfigTaskFrequencyClick()
            is ItemTaskConfig.Reminders -> onConfigTaskRemindersClick()
            is ItemTaskConfig.Tags -> onConfigTaskTagsClick()
            is ItemTaskConfig.Description -> onConfigDescriptionClick()
            is ItemTaskConfig.Priority -> onConfigTaskPriorityClick()
        }
    }

    private fun onConfigTaskPriorityClick() {
        taskWithContentState.value?.task?.priority?.let { priority ->
            setUpConfigState(
                CreateTaskScreenConfig.PickTaskPriority(
                    stateHolder = PickTaskPriorityStateHolder(
                        initPriority = priority,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onConfigDescriptionClick() {
        taskWithContentState.value?.task?.description?.let { description ->
            setUpConfigState(
                CreateTaskScreenConfig.PickTaskDescription(
                    stateHolder = PickTaskDescriptionStateHolder(
                        initDescription = description,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onConfigDateClick(itemConfig: ItemTaskConfig.Date) {
        when (itemConfig) {
            is ItemTaskConfig.Date.StartDate -> onConfigStartDateClick()
            is ItemTaskConfig.Date.EndDate -> onConfigEndDateClick()
            is ItemTaskConfig.Date.OneDayDate -> onConfigOneDayDate()
        }
    }

    private fun onConfigOneDayDate() {
        taskWithContentState.value?.task?.let { task ->
            setUpConfigState(
                CreateTaskScreenConfig.PickDate.OneDayDate(
                    stateHolder = PickDateStateHolder(
                        requestModel = PickDateRequestModel(
                            currentDate = task.startDate,
                            minDate = task.startDate.minus(1, DateTimeUnit.YEAR),
                            maxDate = task.startDate.plus(1, DateTimeUnit.YEAR)
                        ),
                        holderScope = provideChildScope(),
                        defaultDispatcher = defaultDispatcher
                    )
                )
            )
        }
    }

    private fun onConfigEndDateClick() {
        taskWithContentState.value?.task?.let { task ->
            setUpConfigState(
                CreateTaskScreenConfig.PickDate.EndDate(
                    stateHolder = PickDateStateHolder(
                        requestModel = PickDateRequestModel(
                            currentDate = task.endDate ?: task.startDate,
                            minDate = task.startDate,
                            maxDate = task.startDate.plus(1, DateTimeUnit.YEAR)
                        ),
                        holderScope = provideChildScope(),
                        defaultDispatcher = defaultDispatcher
                    )
                )
            )
        }
    }

    private fun onConfigStartDateClick() {
        taskWithContentState.value?.task?.let { task ->
            setUpConfigState(
                CreateTaskScreenConfig.PickDate.StartDate(
                    stateHolder = PickDateStateHolder(
                        requestModel = PickDateRequestModel(
                            currentDate = task.startDate,
                            minDate = todayDate,
                            maxDate = task.endDate ?: task.startDate.plus(1, DateTimeUnit.YEAR)
                        ),
                        holderScope = provideChildScope(),
                        defaultDispatcher = defaultDispatcher
                    )
                )
            )
        }
    }

    private fun onConfigTaskTagsClick() {
        setUpConfigState(
            CreateTaskScreenConfig.PickTaskTags(
                stateHolder = PickTaskTagsStateHolder(
                    allTags = allTagsState.value,
                    initSelectedTagIds = taskTagIdsState.value,
                    holderScope = provideChildScope()
                )
            )
        )
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

            is CreateTaskScreenEvent.ResultEvent.PickTaskTags ->
                onPickTaskTagsResultEvent(event)

            is CreateTaskScreenEvent.ResultEvent.PickDate ->
                onPickDateResultEvent(event)

            is CreateTaskScreenEvent.ResultEvent.PickTaskDescription ->
                onPickTaskDescriptionResultEvent(event)

            is CreateTaskScreenEvent.ResultEvent.PickTaskPriority ->
                onPickTaskPriorityResultEvent(event)

            is CreateTaskScreenEvent.ResultEvent.ConfirmLeave ->
                onConfirmLeaveResultEvent(event)
        }
    }

    private fun onConfirmLeaveResultEvent(event: CreateTaskScreenEvent.ResultEvent.ConfirmLeave) {
        onIdleToAction {
            when (val result = event.result) {
                is ConfirmLeaveScreenResult.Confirm -> onConfirmLeave(result)
                is ConfirmLeaveScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmLeave(result: ConfirmLeaveScreenResult.Confirm) {
        setUpNavigationState(CreateTaskScreenNavigation.Back)
    }

    private fun onPickTaskPriorityResultEvent(event: CreateTaskScreenEvent.ResultEvent.PickTaskPriority) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskPriorityScreenResult.Confirm -> onConfirmTaskPriority(result)
                is PickTaskPriorityScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmTaskPriority(result: PickTaskPriorityScreenResult.Confirm) {
        viewModelScope.launch {
            updateTaskPriorityByIdUseCase(
                taskId = taskId,
                priority = result.priority
            )
        }
    }

    private fun onPickTaskDescriptionResultEvent(event: CreateTaskScreenEvent.ResultEvent.PickTaskDescription) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskDescriptionScreenResult.Confirm -> onConfirmPickTaskDescription(result)
                is PickTaskDescriptionScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskDescription(result: PickTaskDescriptionScreenResult.Confirm) {
        viewModelScope.launch {
            updateTaskDescriptionByIdUseCase(
                taskId = taskId,
                description = result.description
            )
        }
    }

    private fun onPickDateResultEvent(event: CreateTaskScreenEvent.ResultEvent.PickDate) {
        onIdleToAction {
            when (val result = event.result) {
                is PickDateScreenResult.Confirm -> {
                    when (event) {
                        is CreateTaskScreenEvent.ResultEvent.PickDate.StartDate ->
                            onConfirmPickStartDate(result)

                        is CreateTaskScreenEvent.ResultEvent.PickDate.EndDate ->
                            onConfirmPickEndDate(result)

                        is CreateTaskScreenEvent.ResultEvent.PickDate.OneDayDate ->
                            onConfirmPickOneDayDate(result)
                    }
                }

                is PickDateScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickStartDate(result: PickDateScreenResult.Confirm) {
        viewModelScope.launch {
            updateTaskDateUseCase(
                taskId = taskId,
                requestBody = UpdateTaskDateUseCase.RequestBody.StartDate(
                    date = result.date
                )
            )
        }
    }

    private fun onConfirmPickEndDate(result: PickDateScreenResult.Confirm) {
        viewModelScope.launch {
            updateTaskDateUseCase(
                taskId = taskId,
                requestBody = UpdateTaskDateUseCase.RequestBody.EndDate(
                    date = result.date
                )
            )
        }
    }

    private fun onConfirmPickOneDayDate(result: PickDateScreenResult.Confirm) {
        viewModelScope.launch {
            updateTaskDateUseCase(
                taskId = taskId,
                requestBody = UpdateTaskDateUseCase.RequestBody.OneDayDate(
                    date = result.date
                )
            )
        }
    }

    private fun onPickTaskTagsResultEvent(event: CreateTaskScreenEvent.ResultEvent.PickTaskTags) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskTagsScreenResult.Confirm -> onConfirmPickTaskTags(result)
                is PickTaskTagsScreenResult.ManageTags -> onManageTags()
                is PickTaskTagsScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskTags(result: PickTaskTagsScreenResult.Confirm) {
        viewModelScope.launch {
            saveTagCrossByTaskIdUseCase(
                taskId = taskId,
                allTagIds = result.tagIds
            )
        }
    }

    private fun onManageTags() {
        setUpNavigationState(CreateTaskScreenNavigation.ViewTags)
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
        if (taskWithContentState.value?.task?.title?.isNotEmpty() == true) {
            setUpConfigState(CreateTaskScreenConfig.ConfirmLeave)
        } else {
            setUpNavigationState(CreateTaskScreenNavigation.Back)
        }
    }

    private suspend fun provideTaskConfigItems(
        taskWithContentModel: TaskWithContentModel?,
        taskRemindersCount: Int,
        taskTagsCount: Int
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
                    add(ItemTaskConfig.Tags(taskTagsCount))
                    add(ItemTaskConfig.Priority(taskWithContentModel.task.priority))

                }.sortedBy { it.key.ordinal }
            } else emptyList()
        }

    companion object {
        private const val DEFAULT_REMINDER_COUNT = 0
        private const val DEFAULT_TAG_COUNT = 0
    }

}