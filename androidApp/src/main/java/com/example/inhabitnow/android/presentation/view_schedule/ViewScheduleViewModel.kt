package com.example.inhabitnow.android.presentation.view_schedule

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.common.pick_date.PickDateStateHolder
import com.example.inhabitnow.android.presentation.common.pick_date.components.PickDateScreenResult
import com.example.inhabitnow.android.presentation.common.pick_date.model.PickDateRequestModel
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenConfig
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenEvent
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenNavigation
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenState
import com.example.inhabitnow.android.presentation.view_schedule.model.FullTaskWithRecordModel
import com.example.inhabitnow.android.presentation.view_schedule.model.ItemDayOfWeek
import com.example.inhabitnow.android.presentation.view_schedule.model.TaskScheduleStatusType
import com.example.inhabitnow.android.presentation.view_schedule.model.TaskWithRecordModel
import com.example.inhabitnow.core.type.ProgressLimitType
import com.example.inhabitnow.domain.model.record.content.RecordContentModel
import com.example.inhabitnow.domain.model.task.TaskModel
import com.example.inhabitnow.domain.model.task.derived.FullTaskModel
import com.example.inhabitnow.domain.use_case.read_full_tasks_by_date.ReadFullTasksByDateUseCase
import com.example.inhabitnow.domain.use_case.record.read_records_by_date.ReadRecordsByDateUseCase
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
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
    @DefaultDispatcherQualifier private val defaultDispatcher: CoroutineDispatcher
) : BaseViewModel<ViewScheduleScreenEvent, ViewScheduleScreenState, ViewScheduleScreenNavigation, ViewScheduleScreenConfig>() {
    private val todayDateState = MutableStateFlow(nowDate)
    private val currentDateState = MutableStateFlow<LocalDate>(todayDateState.value)
    private val currentStartOfWeekDateState = MutableStateFlow(todayDateState.value.firstDayOfWeek)

    private val allDaysOfWeekState = combine(
        currentDateState,
        currentStartOfWeekDateState,
        todayDateState,
    ) { currentDate, currentStartOfWeekDate, todayDate ->
        provideDateItems(
            targetDate = currentStartOfWeekDate,
            currentDate = currentDate,
            todayDate = todayDate
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    private val allTasksState: StateFlow<UIResultModel<List<FullTaskWithRecordModel>>> =
        currentDateState.flatMapLatest { date ->
            combine(
                readFullTasksByDateUseCase(date),
                readRecordsByDateUseCase(date)
            ) { allFullTasks, allRecords ->
                if (allFullTasks.isNotEmpty()) {
                    withContext(defaultDispatcher) {
                        UIResultModel.Data(
                            allFullTasks.map { fullTaskModel ->
                                async {
                                    val recordEntry =
                                        allRecords.find { it.taskId == fullTaskModel.taskModel.id }?.entry
                                    fullTaskModel.toFullTaskModelWithRecord(recordEntry)
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

    private val isLockedState =
        combine(currentDateState, todayDateState) { currentDate, todayDate ->
            currentDate > todayDate
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            false
        )

    override val uiScreenState: StateFlow<ViewScheduleScreenState> =
        combine(
            allTasksState,
            currentDateState,
            allDaysOfWeekState,
            isLockedState
        ) { allTasks, currentDate, allDaysOfWeek, isLocked ->
            ViewScheduleScreenState(
                allTasksWithRecord = allTasks,
                currentDate = currentDate,
                allDaysOfWeek = allDaysOfWeek,
                isLocked = isLocked
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ViewScheduleScreenState(
                currentDate = currentDateState.value,
                allTasksWithRecord = allTasksState.value,
                allDaysOfWeek = allDaysOfWeekState.value,
                isLocked = isLockedState.value
            )
        )

    override fun onEvent(event: ViewScheduleScreenEvent) {
        when (event) {
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

            is ViewScheduleScreenEvent.ResultEvent ->
                onResultEvent(event)
        }
    }

    private fun onResultEvent(event: ViewScheduleScreenEvent.ResultEvent) {
        when (event) {
            is ViewScheduleScreenEvent.ResultEvent.PickDate ->
                onPickDateResultEvent(event)
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
        currentStartOfWeekDateState.update { result.date.firstDayOfWeek }
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
        currentStartOfWeekDateState.update { oldDate ->
            oldDate.minus(1, DateTimeUnit.WEEK)
        }
    }

    private fun onNextWeekClick() {
        currentStartOfWeekDateState.update { oldDate ->
            oldDate.plus(1, DateTimeUnit.WEEK)
        }
    }

    private fun FullTaskModel.toFullTaskModelWithRecord(
        recordEntry: RecordContentModel.Entry?
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
                                        statusType = when (entry) {
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
                                        statusType = when (entry) {
                                            null -> TaskScheduleStatusType.Pending
                                            is RecordContentModel.Entry.Skip -> TaskScheduleStatusType.Skipped
                                            is RecordContentModel.Entry.Fail -> TaskScheduleStatusType.Failed
                                            is RecordContentModel.Entry.Time -> {
                                                val limitTime = taskModel.progressContent.limitTime
                                                val entryTime = entry.time
                                                val isDone =
                                                    when (taskModel.progressContent.limitType) {
                                                        ProgressLimitType.AtLeast -> entryTime >= limitTime
                                                        ProgressLimitType.Exactly -> entryTime == limitTime
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
                            statusType = when (entry) {
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
                    statusType = when (entry) {
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
        val finishedStatuses = setOf(
            TaskScheduleStatusType.Done,
            TaskScheduleStatusType.Skipped,
            TaskScheduleStatusType.Failed
        )
        val maxTime = LocalTime(hour = 23, minute = 59, second = 59)
        allTasks.sortedWith(
            compareBy<FullTaskWithRecordModel> { fullTaskWithRecord ->
                if (fullTaskWithRecord.taskWithRecordModel.statusType in finishedStatuses) {
                    FINISHED_WEIGHT
                } else {
                    PENDING_WEIGHT
                }
            }.then(compareBy { fullTaskWithRecord ->
                fullTaskWithRecord.allReminders.minByOrNull { it.time }?.time ?: maxTime
            }).then(compareByDescending { fullTaskWithRecord ->
                fullTaskWithRecord.taskWithRecordModel.task.priority
            })

        )
    }

    private fun provideDateItems(
        targetDate: LocalDate,
        currentDate: LocalDate,
        todayDate: LocalDate
    ): List<ItemDayOfWeek> = targetDate.firstDayOfWeek.let { startOfWeekDate ->
        IntRange(0, DayOfWeek.entries.size - 1).map { offset ->
            startOfWeekDate.plus(offset, DateTimeUnit.DAY).let { nextDate ->
                when (nextDate) {
                    currentDate -> ItemDayOfWeek.Current(nextDate)
                    todayDate -> ItemDayOfWeek.Today(nextDate)
                    else -> ItemDayOfWeek.Day(nextDate)
                }
            }
        }
    }

    private val LocalDate.firstDayOfWeek
        get() = this.let { date -> date.minus(date.dayOfWeek.ordinal, DateTimeUnit.DAY) }

    private val nowDate: LocalDate
        get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    companion object {
        private const val FINISHED_WEIGHT = 1
        private const val PENDING_WEIGHT = 0
    }

}