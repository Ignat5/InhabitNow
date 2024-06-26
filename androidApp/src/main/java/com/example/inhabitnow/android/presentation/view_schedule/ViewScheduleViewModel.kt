package com.example.inhabitnow.android.presentation.view_schedule

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.common.pick_date.PickDateStateHolder
import com.example.inhabitnow.android.presentation.common.pick_date.components.PickDateScreenResult
import com.example.inhabitnow.android.presentation.common.pick_date.model.PickDateRequestModel
import com.example.inhabitnow.android.presentation.main.config.pick_task_progress_type.PickTaskProgressTypeScreenResult
import com.example.inhabitnow.android.presentation.main.config.pick_task_type.PickTaskTypeScreenResult
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenConfig
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenEvent
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenNavigation
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenState
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_number_record.EnterTaskNumberRecordStateHolder
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_number_record.components.EnterTaskNumberRecordScreenResult
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_time_record.EnterTaskTimeRecordStateHolder
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_time_record.components.EnterTaskTimeRecordScreenResult
import com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions.ViewHabitRecordActionsStateHolder
import com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions.components.ViewHabitRecordActionsScreenResult
import com.example.inhabitnow.android.presentation.view_schedule.model.FullTaskWithRecordModel
import com.example.inhabitnow.android.presentation.view_schedule.model.TaskScheduleStatusType
import com.example.inhabitnow.android.presentation.view_schedule.model.TaskWithRecordModel
import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.core.type.ProgressLimitType
import com.example.inhabitnow.core.type.TaskProgressType
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.domain.model.record.content.RecordContentModel
import com.example.inhabitnow.domain.model.task.TaskModel
import com.example.inhabitnow.domain.model.task.derived.FullTaskModel
import com.example.inhabitnow.domain.use_case.read_full_tasks_by_date.ReadFullTasksByDateUseCase
import com.example.inhabitnow.domain.use_case.record.read_records_by_date.ReadRecordsByDateUseCase
import com.example.inhabitnow.domain.use_case.record.save_record.SaveRecordUseCase
import com.example.inhabitnow.domain.use_case.save_default_task.SaveDefaultTaskUseCase
import com.example.inhabitnow.domain.use_case.validate_limit_number.ValidateInputLimitNumberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ViewScheduleViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val readFullTasksByDateUseCase: ReadFullTasksByDateUseCase,
    private val readRecordsByDateUseCase: ReadRecordsByDateUseCase,
    private val saveRecordUseCase: SaveRecordUseCase,
    private val saveDefaultTaskUseCase: SaveDefaultTaskUseCase,
    private val validateInputLimitNumberUseCase: ValidateInputLimitNumberUseCase,
    @DefaultDispatcherQualifier private val defaultDispatcher: CoroutineDispatcher
) : BaseViewModel<ViewScheduleScreenEvent, ViewScheduleScreenState, ViewScheduleScreenNavigation, ViewScheduleScreenConfig>() {
    private val todayDateState = MutableStateFlow(nowDate)
    private val currentDateState = MutableStateFlow<LocalDate>(todayDateState.value)
    private val startOfWeekDateState = MutableStateFlow(todayDateState.value.firstDayOfWeek)

    private val isLockedState =
        combine(currentDateState, todayDateState) { currentDate, todayDate ->
            currentDate > todayDate
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            false
        )

    private val allTasksState: StateFlow<UIResultModel<List<FullTaskWithRecordModel>>> =
        currentDateState.flatMapLatest { date ->
            combine(
                readFullTasksByDateUseCase(date),
                readRecordsByDateUseCase(date),
                isLockedState
            ) { allFullTasks, allRecords, isLocked ->
                if (allFullTasks.isNotEmpty()) {
                    withContext(defaultDispatcher) {
                        UIResultModel.Data(
                            allFullTasks.map { fullTaskModel ->
                                async {
                                    val recordEntry =
                                        allRecords.find { it.taskId == fullTaskModel.taskModel.id }?.entry
                                    fullTaskModel.toFullTaskModelWithRecord(recordEntry, isLocked)
                                }
                            }.awaitAll().sortTasks()
                        )
                    }
                } else UIResultModel.NoData
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            UIResultModel.Loading(emptyList())
        )

    override val uiScreenState: StateFlow<ViewScheduleScreenState> =
        combine(
            allTasksState,
            currentDateState,
            startOfWeekDateState,
            todayDateState,
            isLockedState
        ) { allTasks, currentDate, startOfWeekDate, todayDate, isLocked ->
            ViewScheduleScreenState(
                allTasksWithRecord = allTasks,
                currentDate = currentDate,
                startOfWeekDate = startOfWeekDate,
                todayDate = todayDate,
                isLocked = isLocked
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ViewScheduleScreenState(
                currentDate = currentDateState.value,
                allTasksWithRecord = allTasksState.value,
                startOfWeekDate = startOfWeekDateState.value,
                todayDate = todayDateState.value,
                isLocked = isLockedState.value
            )
        )

    override fun onEvent(event: ViewScheduleScreenEvent) {
        when (event) {
            is ViewScheduleScreenEvent.OnTaskClick ->
                onTaskClick(event)

            is ViewScheduleScreenEvent.OnTaskLongClick ->
                onTaskLongClick(event)

            is ViewScheduleScreenEvent.OnCreateTaskClick ->
                onCreateTaskClick()

            is ViewScheduleScreenEvent.ResultEvent ->
                onResultEvent(event)

            is ViewScheduleScreenEvent.OnDateClick ->
                onDateClick(event)

            is ViewScheduleScreenEvent.OnPrevWeekClick ->
                onPrevWeekClick()

            is ViewScheduleScreenEvent.OnNextWeekClick ->
                onNextWeekClick()

            is ViewScheduleScreenEvent.OnSearchClick ->
                onSearchClick()

            is ViewScheduleScreenEvent.OnPickDateClick ->
                onPickDateClick()
        }
    }

    private fun onResultEvent(event: ViewScheduleScreenEvent.ResultEvent) {
        when (event) {
            is ViewScheduleScreenEvent.ResultEvent.PickDate ->
                onPickDateResultEvent(event)

            is ViewScheduleScreenEvent.ResultEvent.EnterTaskNumberRecord ->
                onEnterTaskNumberRecordResultEvent(event)

            is ViewScheduleScreenEvent.ResultEvent.EnterTaskTimeRecord ->
                onEnterTaskTimeRecordResultEvent(event)

            is ViewScheduleScreenEvent.ResultEvent.ViewHabitRecordActions ->
                onViewHabitRecordActionsResultEvent(event)

            is ViewScheduleScreenEvent.ResultEvent.PickTaskType ->
                onPickTaskTypeResultEvent(event)

            is ViewScheduleScreenEvent.ResultEvent.PickTaskProgressType ->
                onPickTaskProgressTypeResultEvent(event)
        }
    }

    private fun onPickTaskProgressTypeResultEvent(event: ViewScheduleScreenEvent.ResultEvent.PickTaskProgressType) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskProgressTypeScreenResult.Confirm ->
                    onConfirmPickTaskProgressType(result)

                is PickTaskProgressTypeScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskProgressType(result: PickTaskProgressTypeScreenResult.Confirm) {
        viewModelScope.launch {
            val resultModel = saveDefaultTaskUseCase(
                SaveDefaultTaskUseCase.RequestType.CreateHabit(result.taskProgressType)
            )
            when (resultModel) {
                is ResultModel.Success -> {
                    val taskId = resultModel.data
                    setUpNavigationState(ViewScheduleScreenNavigation.CreateTask(taskId))
                }

                is ResultModel.Error -> Unit
            }
        }
    }

    private fun onPickTaskTypeResultEvent(event: ViewScheduleScreenEvent.ResultEvent.PickTaskType) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskTypeScreenResult.Confirm -> onConfirmPickTaskType(result)
                is PickTaskTypeScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskType(result: PickTaskTypeScreenResult.Confirm) {
        when (val taskType = result.taskType) {
            TaskType.Habit -> {
                setUpConfigState(
                    ViewScheduleScreenConfig.PickTaskProgressType(
                        TaskProgressType.entries
                    )
                )
            }

            TaskType.RecurringTask, TaskType.SingleTask -> {
                viewModelScope.launch {
                    val requestType = when (taskType) {
                        TaskType.RecurringTask -> SaveDefaultTaskUseCase.RequestType.CreateRecurringTask
                        TaskType.SingleTask -> SaveDefaultTaskUseCase.RequestType.CreateTask
                        else -> throw IllegalStateException()
                    }
                    when (val resultModel = saveDefaultTaskUseCase(requestType)) {
                        is ResultModel.Success -> {
                            val taskId = resultModel.data
                            setUpNavigationState(
                                ViewScheduleScreenNavigation.CreateTask(taskId)
                            )
                        }

                        is ResultModel.Error -> Unit
                    }
                }
            }
        }
    }

    private fun onViewHabitRecordActionsResultEvent(event: ViewScheduleScreenEvent.ResultEvent.ViewHabitRecordActions) {
        onIdleToAction {
            when (val result = event.result) {
                is ViewHabitRecordActionsScreenResult.Confirm ->
                    onConfirmViewHabitRecordActions(result)

                is ViewHabitRecordActionsScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmViewHabitRecordActions(result: ViewHabitRecordActionsScreenResult.Confirm) {
        when (result.action) {
            is ViewHabitRecordActionsScreenResult.Action.Edit ->
                onEditTaskClick(result.taskId)

            is ViewHabitRecordActionsScreenResult.Action.EnterRecord ->
                onEnterRecordClick(result.taskId)

            is ViewHabitRecordActionsScreenResult.Action.Done ->
                onDoneTaskClick(result.taskId)

            is ViewHabitRecordActionsScreenResult.Action.Fail ->
                onFailTaskClick(result.taskId)

            is ViewHabitRecordActionsScreenResult.Action.Skip ->
                onSkipTaskClick(result.taskId)

            is ViewHabitRecordActionsScreenResult.Action.ResetEntry ->
                onResetTaskEntryClick(result.taskId)
        }
    }

    private fun onDoneTaskClick(taskId: String) {
        viewModelScope.launch {
            saveRecordUseCase(
                taskId = taskId,
                targetDate = currentDateState.value,
                requestType = SaveRecordUseCase.RequestType.EntryDone
            )
        }
    }

    private fun onFailTaskClick(taskId: String) {
        viewModelScope.launch {
            saveRecordUseCase(
                taskId = taskId,
                targetDate = currentDateState.value,
                requestType = SaveRecordUseCase.RequestType.EntryFail
            )
        }
    }

    private fun onSkipTaskClick(taskId: String) {
        viewModelScope.launch {
            saveRecordUseCase(
                taskId = taskId,
                targetDate = currentDateState.value,
                requestType = SaveRecordUseCase.RequestType.EntrySkip
            )
        }
    }

    private fun onResetTaskEntryClick(taskId: String) {
        viewModelScope.launch {
            saveRecordUseCase(
                taskId = taskId,
                targetDate = currentDateState.value,
                requestType = SaveRecordUseCase.RequestType.EntryReset
            )
        }
    }

    private fun onEditTaskClick(taskId: String) {
        setUpNavigationState(ViewScheduleScreenNavigation.EditTask(taskId))
    }

    private fun onEnterRecordClick(taskId: String) {
        allTasksState.value.data?.find { it.taskWithRecordModel.task.id == taskId }?.taskWithRecordModel?.let { taskWithRecord ->
            when (taskWithRecord) {
                is TaskWithRecordModel.Habit.HabitContinuous -> {
                    when (taskWithRecord) {
                        is TaskWithRecordModel.Habit.HabitContinuous.HabitNumber -> {
                            onEnterHabitNumberRecordClick(taskWithRecord)
                        }

                        is TaskWithRecordModel.Habit.HabitContinuous.HabitTime -> {
                            onEnterHabitTimeRecordClick(taskWithRecord)
                        }
                    }
                }

                else -> Unit
            }
        }
    }

    private fun onEnterHabitNumberRecordClick(taskWithRecord: TaskWithRecordModel.Habit.HabitContinuous.HabitNumber) {
        setUpConfigState(
            ViewScheduleScreenConfig.EnterTaskNumberRecord(
                stateHolder = EnterTaskNumberRecordStateHolder(
                    taskWithRecord = taskWithRecord,
                    date = currentDateState.value,
                    validateInputLimitNumberUseCase = validateInputLimitNumberUseCase,
                    holderScope = provideChildScope()
                )
            )
        )
    }

    private fun onEnterHabitTimeRecordClick(taskWithRecord: TaskWithRecordModel.Habit.HabitContinuous.HabitTime) {
        setUpConfigState(
            ViewScheduleScreenConfig.EnterTaskTimeRecord(
                stateHolder = EnterTaskTimeRecordStateHolder(
                    taskWithRecordModel = taskWithRecord,
                    date = currentDateState.value,
                    holderScope = provideChildScope()
                )
            )
        )
    }

    private fun onEnterHabitYesNoRecordClick(taskWithRecord: TaskWithRecordModel.Habit.HabitYesNo) {
        viewModelScope.launch {
            saveRecordUseCase(
                taskId = taskWithRecord.task.id,
                targetDate = currentDateState.value,
                requestType = SaveRecordUseCase.RequestType.EntryDone
            )
        }
    }

    private fun onEnterTaskTimeRecordResultEvent(event: ViewScheduleScreenEvent.ResultEvent.EnterTaskTimeRecord) {
        onIdleToAction {
            when (val result = event.result) {
                is EnterTaskTimeRecordScreenResult.Confirm ->
                    onConfirmEnterTaskTimeRecord(result)

                is EnterTaskTimeRecordScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmEnterTaskTimeRecord(result: EnterTaskTimeRecordScreenResult.Confirm) {
        viewModelScope.launch {
            saveRecordUseCase(
                taskId = result.taskId,
                targetDate = result.date,
                requestType = SaveRecordUseCase.RequestType.EntryContinuous(
                    entry = RecordContentModel.Entry.Time(result.time)
                ),
            )
        }
    }

    private fun onEnterTaskNumberRecordResultEvent(event: ViewScheduleScreenEvent.ResultEvent.EnterTaskNumberRecord) {
        onIdleToAction {
            when (val result = event.result) {
                is EnterTaskNumberRecordScreenResult.Confirm ->
                    onConfirmEnterTaskNumberRecord(result)

                is EnterTaskNumberRecordScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmEnterTaskNumberRecord(result: EnterTaskNumberRecordScreenResult.Confirm) {
        viewModelScope.launch {
            saveRecordUseCase(
                taskId = result.taskId,
                targetDate = result.date,
                requestType = SaveRecordUseCase.RequestType.EntryContinuous(
                    entry = RecordContentModel.Entry.Number(result.number)
                )
            )
        }
    }

    private fun onPickDateResultEvent(event: ViewScheduleScreenEvent.ResultEvent.PickDate) {
        onIdleToAction {
            when (val result = event.result) {
                is PickDateScreenResult.Confirm -> onConfirmPickDate(result)
                is PickDateScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickDate(result: PickDateScreenResult.Confirm) {
        currentDateState.update { result.date }
        startOfWeekDateState.update { result.date.firstDayOfWeek }
    }

    private fun onTaskClick(event: ViewScheduleScreenEvent.OnTaskClick) {
        allTasksState.value.data?.find { it.taskWithRecordModel.task.id == event.taskId }?.taskWithRecordModel?.let { taskWithRecord ->
            if (!isLockedState.value) {
                when (taskWithRecord) {
                    is TaskWithRecordModel.Habit -> onHabitClick(taskWithRecord)
                    is TaskWithRecordModel.Task -> onTaskClick(taskWithRecord)
                }
            } else {
                setUpNavigationState(
                    ViewScheduleScreenNavigation.EditTask(
                        taskWithRecord.task.id
                    )
                )
            }
        }
    }

    private fun onHabitClick(taskWithRecordModel: TaskWithRecordModel.Habit) {
        when (taskWithRecordModel) {
            is TaskWithRecordModel.Habit.HabitContinuous -> {
                when (taskWithRecordModel) {
                    is TaskWithRecordModel.Habit.HabitContinuous.HabitNumber -> {
                        onHabitNumberClick(taskWithRecordModel)
                    }

                    is TaskWithRecordModel.Habit.HabitContinuous.HabitTime -> {
                        onHabitTimeClick(taskWithRecordModel)
                    }
                }
            }

            is TaskWithRecordModel.Habit.HabitYesNo -> {
                onHabitYesNoClick(taskWithRecordModel)
            }
        }
    }

    private fun onHabitNumberClick(taskWithRecordModel: TaskWithRecordModel.Habit.HabitContinuous.HabitNumber) {
        setUpConfigState(
            ViewScheduleScreenConfig.EnterTaskNumberRecord(
                stateHolder = EnterTaskNumberRecordStateHolder(
                    taskWithRecord = taskWithRecordModel,
                    date = currentDateState.value,
                    validateInputLimitNumberUseCase = validateInputLimitNumberUseCase,
                    holderScope = provideChildScope()
                )
            )
        )
    }

    private fun onHabitTimeClick(taskWithRecordModel: TaskWithRecordModel.Habit.HabitContinuous.HabitTime) {
        setUpConfigState(
            ViewScheduleScreenConfig.EnterTaskTimeRecord(
                stateHolder = EnterTaskTimeRecordStateHolder(
                    taskWithRecordModel = taskWithRecordModel,
                    date = currentDateState.value,
                    holderScope = provideChildScope()
                )
            )
        )
    }

    private fun onHabitYesNoClick(taskWithRecordModel: TaskWithRecordModel.Habit.HabitYesNo) {
        viewModelScope.launch {
            saveRecordUseCase(
                taskId = taskWithRecordModel.task.id,
                targetDate = currentDateState.value,
                requestType = SaveRecordUseCase.RequestType.EntryYesNo
            )
        }
    }

    private fun onTaskClick(taskWithRecordModel: TaskWithRecordModel.Task) {
        viewModelScope.launch {
            saveRecordUseCase(
                taskId = taskWithRecordModel.task.id,
                targetDate = currentDateState.value,
                requestType = SaveRecordUseCase.RequestType.EntryYesNo
            )
        }
    }

    private fun onTaskLongClick(event: ViewScheduleScreenEvent.OnTaskLongClick) {
        allTasksState.value.data?.find { it.taskWithRecordModel.task.id == event.taskId }?.taskWithRecordModel?.let { taskWithRecord ->
            if (!isLockedState.value) {
                when (taskWithRecord) {
                    is TaskWithRecordModel.Habit -> {
                        onHabitLongClick(taskWithRecord)
                    }

                    is TaskWithRecordModel.Task -> {
                        onTaskLongClick(taskWithRecord)
                    }
                }
            } else setUpNavigationState(ViewScheduleScreenNavigation.EditTask(taskWithRecord.task.id))
        }
    }

    private fun onHabitLongClick(taskWIthRecordModel: TaskWithRecordModel.Habit) {
        setUpConfigState(
            ViewScheduleScreenConfig.ViewHabitRecordActions(
                stateHolder = ViewHabitRecordActionsStateHolder(
                    taskWithRecord = taskWIthRecordModel,
                    date = currentDateState.value,
                    holderScope = provideChildScope()
                )
            )
        )
    }

    private fun onTaskLongClick(taskWithRecord: TaskWithRecordModel.Task) {
        setUpNavigationState(
            ViewScheduleScreenNavigation.EditTask(
                taskWithRecord.task.id
            )
        )
    }

    private fun onCreateTaskClick() {
        setUpConfigState(ViewScheduleScreenConfig.PickTaskType(TaskType.entries))
    }

    private fun onPickDateClick() {
        currentDateState.value.let { currentDate ->
            setUpConfigState(
                ViewScheduleScreenConfig.PickDate(
                    stateHolder = PickDateStateHolder(
                        requestModel = PickDateRequestModel(
                            currentDate = currentDate,
                            minDate = currentDate.minus(1, DateTimeUnit.YEAR),
                            maxDate = currentDate.plus(1, DateTimeUnit.YEAR)
                        ),
                        holderScope = provideChildScope(),
                        defaultDispatcher = defaultDispatcher
                    )
                )
            )
        }
    }

    private fun onSearchClick() {
        setUpNavigationState(ViewScheduleScreenNavigation.Search)
    }

    private fun onDateClick(event: ViewScheduleScreenEvent.OnDateClick) {
        currentDateState.update { event.date }
    }

    private fun onPrevWeekClick() {
        startOfWeekDateState.update { oldDate ->
            oldDate.minus(1, DateTimeUnit.WEEK)
        }
    }

    private fun onNextWeekClick() {
        startOfWeekDateState.update { oldDate ->
            oldDate.plus(1, DateTimeUnit.WEEK)
        }
    }

    private fun FullTaskModel.toFullTaskModelWithRecord(
        recordEntry: RecordContentModel.Entry?,
        isLocked: Boolean
    ): FullTaskWithRecordModel = this.let { fullTaskModel ->
        when (val taskModel = fullTaskModel.taskModel) {
            is TaskModel.Habit -> {
                when (taskModel) {
                    is TaskModel.Habit.HabitContinuous -> {
                        when (taskModel) {
                            is TaskModel.Habit.HabitContinuous.HabitNumber -> {
                                val entry =
                                    recordEntry as? RecordContentModel.Entry.HabitEntry.HabitContinuousEntry.Number
                                val taskWithRecord =
                                    TaskWithRecordModel.Habit.HabitContinuous.HabitNumber(
                                        task = taskModel,
                                        recordEntry = entry,
                                        statusType = if (isLocked) TaskScheduleStatusType.Locked else
                                            when (entry) {
                                                null -> TaskScheduleStatusType.Pending
                                                is RecordContentModel.Entry.Skip -> TaskScheduleStatusType.Skipped
                                                is RecordContentModel.Entry.Fail -> TaskScheduleStatusType.Failed
                                                is RecordContentModel.Entry.Number -> {
                                                    val limitNumber =
                                                        taskModel.progressContent.limitNumber
                                                    val entryNumber = entry.number
                                                    val isDone =
                                                        when (taskModel.progressContent.limitType) {
                                                            ProgressLimitType.AtLeast -> entryNumber >= limitNumber
                                                            ProgressLimitType.Exactly -> entryNumber == limitNumber
                                                            ProgressLimitType.NoMoreThan -> entryNumber <= limitNumber
                                                        }
                                                    if (isDone) TaskScheduleStatusType.Done
                                                    else TaskScheduleStatusType.InProgress
                                                }
                                            }
                                    )
                                FullTaskWithRecordModel(
                                    taskWithRecordModel = taskWithRecord,
                                    allReminders = fullTaskModel.allReminders,
                                    allTags = fullTaskModel.allTags
                                )
                            }

                            is TaskModel.Habit.HabitContinuous.HabitTime -> {
                                val entry =
                                    recordEntry as? RecordContentModel.Entry.HabitEntry.HabitContinuousEntry.Time
                                val taskWithRecordModel =
                                    TaskWithRecordModel.Habit.HabitContinuous.HabitTime(
                                        task = taskModel,
                                        recordEntry = entry,
                                        statusType = if (isLocked) TaskScheduleStatusType.Locked else
                                            when (entry) {
                                                null -> TaskScheduleStatusType.Pending
                                                is RecordContentModel.Entry.Skip -> TaskScheduleStatusType.Skipped
                                                is RecordContentModel.Entry.Fail -> TaskScheduleStatusType.Failed
                                                is RecordContentModel.Entry.Time -> {
                                                    val limitTime =
                                                        taskModel.progressContent.limitTime
                                                    val entryTime = entry.time
                                                    val isDone =
                                                        when (taskModel.progressContent.limitType) {
                                                            ProgressLimitType.AtLeast -> entryTime >= limitTime
                                                            ProgressLimitType.Exactly -> entryTime == limitTime
                                                            ProgressLimitType.NoMoreThan -> entryTime <= limitTime
                                                        }
                                                    if (isDone) TaskScheduleStatusType.Done
                                                    else TaskScheduleStatusType.InProgress
                                                }
                                            }
                                    )
                                FullTaskWithRecordModel(
                                    taskWithRecordModel = taskWithRecordModel,
                                    allReminders = fullTaskModel.allReminders,
                                    allTags = fullTaskModel.allTags
                                )
                            }
                        }
                    }

                    is TaskModel.Habit.HabitYesNo -> {
                        val entry =
                            recordEntry as? RecordContentModel.Entry.HabitEntry.HabitYesNoEntry
                        val taskWithContentModel = TaskWithRecordModel.Habit.HabitYesNo(
                            task = taskModel,
                            recordEntry = entry,
                            statusType = if (isLocked) TaskScheduleStatusType.Locked else
                                when (entry) {
                                    null -> TaskScheduleStatusType.Pending
                                    is RecordContentModel.Entry.Skip -> TaskScheduleStatusType.Skipped
                                    is RecordContentModel.Entry.Fail -> TaskScheduleStatusType.Failed
                                    is RecordContentModel.Entry.Done -> TaskScheduleStatusType.Done
                                }
                        )
                        FullTaskWithRecordModel(
                            taskWithRecordModel = taskWithContentModel,
                            allReminders = fullTaskModel.allReminders,
                            allTags = fullTaskModel.allTags
                        )
                    }
                }
            }

            is TaskModel.Task -> {
                val entry = recordEntry as? RecordContentModel.Entry.TaskEntry
                val taskWithRecord = TaskWithRecordModel.Task(
                    task = taskModel,
                    recordEntry = entry,
                    statusType = if (isLocked) TaskScheduleStatusType.Locked else
                        when (entry) {
                            null -> TaskScheduleStatusType.Pending
                            RecordContentModel.Entry.Done -> TaskScheduleStatusType.Done
                        }
                )
                FullTaskWithRecordModel(
                    taskWithRecordModel = taskWithRecord,
                    allReminders = fullTaskModel.allReminders,
                    allTags = fullTaskModel.allTags
                )
            }
        }
    }

    private fun List<FullTaskWithRecordModel>.sortTasks() = this.let { allTasks ->
        allTasks.sortedWith(
            compareBy<FullTaskWithRecordModel> { fullTaskWithRecord ->
                when (fullTaskWithRecord.taskWithRecordModel.statusType) {
                    TaskScheduleStatusType.Done, TaskScheduleStatusType.Skipped, TaskScheduleStatusType.Failed ->
                        FINISHED_WEIGHT

                    else -> PENDING_WEIGHT
                }
            }.thenBy { fullTaskWithRecord ->
                fullTaskWithRecord.allReminders.minByOrNull { it.time }?.time ?: defaultReminderTime
            }.thenBy { fullTaskWithRecord ->
                -fullTaskWithRecord.taskWithRecordModel.task.priority
            }
        )
    }

    private val LocalDate.firstDayOfWeek
        get() = this.let { date -> date.minus(date.dayOfWeek.ordinal, DateTimeUnit.DAY) }

    private val nowDate: LocalDate
        get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    private val defaultReminderTime by lazy {
        LocalTime(hour = 23, minute = 59, second = 59)
    }

    companion object {
        private const val FINISHED_WEIGHT = 1
        private const val PENDING_WEIGHT = 0
    }

}