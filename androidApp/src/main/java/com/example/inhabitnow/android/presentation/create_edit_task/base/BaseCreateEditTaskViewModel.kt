package com.example.inhabitnow.android.presentation.create_edit_task.base

import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig
import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.common.pick_date.PickDateStateHolder
import com.example.inhabitnow.android.presentation.common.pick_date.components.PickDateScreenResult
import com.example.inhabitnow.android.presentation.common.pick_date.model.PickDateRequestModel
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenConfig
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.model.BaseItemTaskConfig
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description.PickTaskDescriptionStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description.components.PickTaskDescriptionScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.PickTaskFrequencyStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.components.PickTaskFrequencyScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_priority.PickTaskPriorityStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_priority.components.PickTaskPriorityScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number.PickTaskNumberProgressStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number.components.PickTaskNumberProgressScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.time.PickTaskTimeProgressStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.time.components.PickTaskTimeProgressScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.PickTaskTagsStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.components.PickTaskTagsScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.PickTaskTitleStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.components.PickTaskTitleScreenResult
import com.example.inhabitnow.android.presentation.model.UITaskContent
import com.example.inhabitnow.android.ui.toFrequencyContent
import com.example.inhabitnow.android.ui.toUIFrequencyContent
import com.example.inhabitnow.android.ui.toUIProgressContent
import com.example.inhabitnow.domain.model.task.TaskModel
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
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

abstract class BaseCreateEditTaskViewModel<SE : ScreenEvent, SS : ScreenState, SN : ScreenNavigation, SC : ScreenConfig>(
    protected val taskId: String,
    readTaskWithContentByIdUseCase: ReadTaskWithContentByIdUseCase,
    readRemindersCountByTaskIdUseCase: ReadRemindersCountByTaskIdUseCase,
    readTagsUseCase: ReadTagsUseCase,
    readTagIdsByTaskIdUseCase: ReadTagIdsByTaskIdUseCase,
    private val updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase,
    private val updateTaskDescriptionByIdUseCase: UpdateTaskDescriptionByIdUseCase,
    private val updateTaskPriorityByIdUseCase: UpdateTaskPriorityByIdUseCase,
    private val updateTaskProgressByIdUseCase: UpdateTaskProgressByIdUseCase,
    private val updateTaskFrequencyByIdUseCase: UpdateTaskFrequencyByIdUseCase,
    private val updateTaskDateUseCase: UpdateTaskDateUseCase,
    private val saveTagCrossByTaskIdUseCase: SaveTagCrossByTaskIdUseCase,
    private val defaultDispatcher: CoroutineDispatcher
) : BaseViewModel<SE, SS, SN, SC>() {

    private val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    protected val taskModelState = readTaskWithContentByIdUseCase(taskId)
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

    protected val allTaskConfigItemsState = combine(
        taskModelState,
        taskRemindersCountState,
        taskTagsCountState
    ) { taskModel, taskRemindersCount, taskTagsCount ->
        provideBaseTaskConfigItems(
            taskModel = taskModel,
            taskRemindersCount = taskRemindersCount,
            taskTagsCount = taskTagsCount
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    protected fun onBaseEvent(event: BaseCreateEditTaskScreenEvent) {
        when (event) {
            is BaseCreateEditTaskScreenEvent.OnBaseItemTaskConfigClick ->
                onBaseItemTaskConfigClick(event)

            is BaseCreateEditTaskScreenEvent.ResultEvent ->
                onResultEvent(event)
        }
    }

    private fun onBaseItemTaskConfigClick(event: BaseCreateEditTaskScreenEvent.OnBaseItemTaskConfigClick) {
        when (val item = event.item) {
            is BaseItemTaskConfig.Title -> onConfigTaskTitleClick()
            is BaseItemTaskConfig.Description -> onConfigTaskDescriptionClick()
            is BaseItemTaskConfig.Progress.Number -> onConfigTaskNumberProgressClick()
            is BaseItemTaskConfig.Progress.Time -> onConfigTaskTimeProgressClick()
            is BaseItemTaskConfig.Frequency -> onConfigTaskFrequencyClick()
            is BaseItemTaskConfig.Date -> onConfigDateClick(item)
            is BaseItemTaskConfig.Priority -> onConfigTaskPriorityClick()
            is BaseItemTaskConfig.Tags -> onConfigTaskTagsClick()
            is BaseItemTaskConfig.Reminders -> onConfigTaskRemindersClick()
        }
    }

    private fun onConfigTaskTagsClick() {
        setUpBaseConfigState(
            BaseCreateEditTaskScreenConfig.PickTaskTags(
                stateHolder = PickTaskTagsStateHolder(
                    allTags = allTagsState.value,
                    initSelectedTagIds = taskTagIdsState.value,
                    holderScope = provideChildScope()
                )
            )
        )
    }

    private fun onConfigTaskRemindersClick() {
        setUpBaseNavigationState(
            BaseCreateEditTaskScreenNavigation.ViewTaskReminders(
                taskId
            )
        )
    }

    private fun onConfigTaskPriorityClick() {
        taskModelState.value?.priority?.let { priority ->
            setUpBaseConfigState(
                BaseCreateEditTaskScreenConfig.PickTaskPriority(
                    stateHolder = PickTaskPriorityStateHolder(
                        initPriority = priority,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onConfigDateClick(item: BaseItemTaskConfig.Date) {
        when (item) {
            is BaseItemTaskConfig.Date.StartDate -> onConfigStartDateClick()
            is BaseItemTaskConfig.Date.EndDate -> onConfigEndDateClick()
            is BaseItemTaskConfig.Date.OneDayDate -> onConfigOneDayDateClick()
        }
    }

    private fun onConfigOneDayDateClick() {
        (taskModelState.value?.dateContent as? TaskContentModel.DateContent.Day)?.date?.let { date ->
            setUpBaseConfigState(
                BaseCreateEditTaskScreenConfig.PickDate.OneDayDate(
                    stateHolder = PickDateStateHolder(
                        requestModel = PickDateRequestModel(
                            currentDate = date,
                            minDate = minOf(date, todayDate),
                            maxDate = date.plus(1, DateTimeUnit.YEAR)
                        ),
                        holderScope = provideChildScope(),
                        defaultDispatcher = defaultDispatcher
                    )
                )
            )
        }
    }

    private fun onConfigEndDateClick() {
        (taskModelState.value?.dateContent as? TaskContentModel.DateContent.Period)?.let { periodDate ->
            if (periodDate.endDate != null) {
                viewModelScope.launch {
                    updateTaskDateUseCase(
                        taskId = taskId,
                        requestType = UpdateTaskDateUseCase.RequestType.EndDate(null)
                    )
                }
            } else {
                setUpBaseConfigState(
                    BaseCreateEditTaskScreenConfig.PickDate.EndDate(
                        stateHolder = PickDateStateHolder(
                            requestModel = PickDateRequestModel(
                                currentDate = periodDate.startDate,
                                minDate = periodDate.startDate,
                                maxDate = periodDate.startDate.plus(1, DateTimeUnit.YEAR)
                            ),
                            holderScope = provideChildScope(),
                            defaultDispatcher = defaultDispatcher
                        )
                    )
                )
            }
        }
    }

    private fun onConfigStartDateClick() {
        (taskModelState.value?.dateContent as? TaskContentModel.DateContent.Period)?.let { periodDate ->
            setUpBaseConfigState(
                BaseCreateEditTaskScreenConfig.PickDate.StartDate(
                    stateHolder = PickDateStateHolder(
                        requestModel = PickDateRequestModel(
                            currentDate = periodDate.startDate,
                            minDate = minOf(periodDate.startDate, todayDate),
                            maxDate = periodDate.endDate ?: periodDate.startDate.plus(
                                1,
                                DateTimeUnit.YEAR
                            )
                        ),
                        holderScope = provideChildScope(),
                        defaultDispatcher = defaultDispatcher
                    )
                )
            )
        }
    }

    private fun onConfigTaskFrequencyClick() {
        (taskModelState.value as? TaskModel.RecurringActivity)?.frequencyContent?.toUIFrequencyContent()
            ?.let { fc ->
                setUpBaseConfigState(
                    BaseCreateEditTaskScreenConfig.PickTaskFrequency(
                        stateHolder = PickTaskFrequencyStateHolder(
                            initFrequency = fc,
                            holderScope = provideChildScope()
                        )
                    )
                )
            }
    }

    private fun onConfigTaskTimeProgressClick() {
        (taskModelState.value as? TaskModel.Habit.HabitContinuous.HabitTime)?.progressContent?.let { pc ->
            setUpBaseConfigState(
                BaseCreateEditTaskScreenConfig.PickTaskTimeProgress(
                    stateHolder = PickTaskTimeProgressStateHolder(
                        initProgressContent = pc,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onConfigTaskNumberProgressClick() {
        (taskModelState.value as? TaskModel.Habit.HabitContinuous.HabitNumber)?.progressContent?.let { pc ->
            setUpBaseConfigState(
                BaseCreateEditTaskScreenConfig.PickTaskNumberProgress(
                    stateHolder = PickTaskNumberProgressStateHolder(
                        initProgressContent = pc,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onConfigTaskDescriptionClick() {
        taskModelState.value?.description?.let { description ->
            setUpBaseConfigState(
                BaseCreateEditTaskScreenConfig.PickTaskDescription(
                    stateHolder = PickTaskDescriptionStateHolder(
                        initDescription = description,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onConfigTaskTitleClick() {
        taskModelState.value?.title?.let { title ->
            setUpBaseConfigState(
                BaseCreateEditTaskScreenConfig.PickTaskTitle(
                    stateHolder = PickTaskTitleStateHolder(
                        initTitle = title,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent) {
        when (event) {
            is BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskTitle ->
                onPickTaskTitleResultEvent(event)

            is BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskDescription ->
                onPickTaskDescriptionResultEvent(event)

            is BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskNumberProgress ->
                onPickTaskNumberProgressResultEvent(event)

            is BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskTimeProgress ->
                onPickTaskTimeProgressResultEvent(event)

            is BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskFrequency ->
                onPickTaskFrequencyResultEvent(event)

            is BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskDate ->
                onPickDateResultEvent(event)

            is BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskPriority ->
                onPickTaskPriorityResultEvent(event)

            is BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskTags ->
                onPickTaskTagsResultEvent(event)
        }
    }

    private fun onPickTaskTagsResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskTags) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskTagsScreenResult.Confirm -> onConfirmPickTaskTags(result)
                is PickTaskTagsScreenResult.ManageTags -> onManageTagsResult()
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

    private fun onManageTagsResult() {
        setUpBaseNavigationState(BaseCreateEditTaskScreenNavigation.ViewTags)
    }

    private fun onPickTaskPriorityResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskPriority) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskPriorityScreenResult.Confirm -> onConfirmPickTaskPriority(result)
                is PickTaskPriorityScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskPriority(result: PickTaskPriorityScreenResult.Confirm) {
        viewModelScope.launch {
            updateTaskPriorityByIdUseCase(
                taskId = taskId,
                priority = result.priority
            )
        }
    }

    private fun onPickDateResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskDate) {
        onIdleToAction {
            when (val result = event.result) {
                is PickDateScreenResult.Confirm -> {
                    when (event) {
                        is BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskDate.StartDate ->
                            onConfirmPickStartDate(result)

                        is BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskDate.EndDate ->
                            onConfirmPickEndDate(result)

                        is BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskDate.OneDayDate ->
                            onConfirmPickOneDayDate(result)
                    }
                }

                is PickDateScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickOneDayDate(result: PickDateScreenResult.Confirm) {
        viewModelScope.launch {
            updateTaskDateUseCase(
                taskId = taskId,
                requestType = UpdateTaskDateUseCase.RequestType.OneDayDate(
                    result.date
                )
            )
        }
    }

    private fun onConfirmPickEndDate(result: PickDateScreenResult.Confirm) {
        viewModelScope.launch {
            updateTaskDateUseCase(
                taskId = taskId,
                requestType = UpdateTaskDateUseCase.RequestType.EndDate(
                    result.date
                )
            )
        }
    }

    private fun onConfirmPickStartDate(result: PickDateScreenResult.Confirm) {
        viewModelScope.launch {
            updateTaskDateUseCase(
                taskId = taskId,
                requestType = UpdateTaskDateUseCase.RequestType.StartDate(
                    date = result.date
                )
            )
        }
    }

    private fun onPickTaskFrequencyResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskFrequency) {
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

    private fun onPickTaskTimeProgressResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskTimeProgress) {
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

    private fun onPickTaskNumberProgressResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskNumberProgress) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskNumberProgressScreenResult.Confirm ->
                    onConfirmPickTaskNumberProgress(result)

                is PickTaskNumberProgressScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskNumberProgress(result: PickTaskNumberProgressScreenResult.Confirm) {
        viewModelScope.launch {
            updateTaskProgressByIdUseCase(
                taskId = taskId,
                progressContent = result.progressContent
            )
        }
    }

    private fun onPickTaskDescriptionResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskDescription) {
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

    private fun onPickTaskTitleResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskTitle) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskTitleScreenResult.Confirm -> onConfirmPickTaskTitle(result)
                is PickTaskTitleScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskTitle(result: PickTaskTitleScreenResult.Confirm) {
        viewModelScope.launch {
            updateTaskTitleByIdUseCase(
                taskId = taskId,
                title = result.title
            )
        }
    }

    protected abstract fun setUpBaseConfigState(baseConfig: BaseCreateEditTaskScreenConfig)
    protected abstract fun setUpBaseNavigationState(baseSN: BaseCreateEditTaskScreenNavigation)

    private suspend fun provideBaseTaskConfigItems(
        taskModel: TaskModel?,
        taskRemindersCount: Int,
        taskTagsCount: Int
    ): List<BaseItemTaskConfig> =
        withContext(defaultDispatcher) {
            if (taskModel != null) {
                mutableListOf<BaseItemTaskConfig>().apply {
                    add(BaseItemTaskConfig.Title(taskModel.title))
                    add(BaseItemTaskConfig.Description(taskModel.description))
                    when (taskModel) {
                        is TaskModel.Habit.HabitContinuous.HabitNumber -> {
                            add(
                                BaseItemTaskConfig.Progress.Number(
                                    taskModel.progressContent.toUIProgressContent()
                                            as UITaskContent.Progress.Number
                                )
                            )
                        }

                        is TaskModel.Habit.HabitContinuous.HabitTime -> {
                            add(
                                BaseItemTaskConfig.Progress.Time(
                                    taskModel.progressContent.toUIProgressContent()
                                            as UITaskContent.Progress.Time
                                )
                            )
                        }

                        else -> Unit
                    }
                    if (taskModel is TaskModel.RecurringActivity) {
                        add(
                            BaseItemTaskConfig.Frequency(
                                taskModel.frequencyContent.toUIFrequencyContent()
                            )
                        )
                    }

                    when (val dc = taskModel.dateContent) {
                        is TaskContentModel.DateContent.Day -> {
                            add(BaseItemTaskConfig.Date.OneDayDate(dc.date))
                        }

                        is TaskContentModel.DateContent.Period -> {
                            add(BaseItemTaskConfig.Date.StartDate(dc.startDate))
                            add(BaseItemTaskConfig.Date.EndDate(dc.endDate))
                        }
                    }
                    add(BaseItemTaskConfig.Reminders(taskRemindersCount))
                    add(BaseItemTaskConfig.Tags(taskTagsCount))
                    add(BaseItemTaskConfig.Priority(taskModel.priority))

                }.sortedBy { it.key.ordinal }
            } else emptyList()
        }

    companion object {
        private const val DEFAULT_REMINDER_COUNT = 0
        private const val DEFAULT_TAG_COUNT = 0
    }

}