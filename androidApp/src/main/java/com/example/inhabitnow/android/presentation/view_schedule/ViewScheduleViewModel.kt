package com.example.inhabitnow.android.presentation.view_schedule

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenConfig
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenEvent
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenNavigation
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenState
import com.example.inhabitnow.android.presentation.view_schedule.model.FullTaskWithRecordModel
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
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
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

    private val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    private val currentDateState = MutableStateFlow<LocalDate>(todayDate)

    private val allTasksState: StateFlow<List<FullTaskWithRecordModel>?> = currentDateState.flatMapLatest { date ->
        combine(
            readFullTasksByDateUseCase(date),
            readRecordsByDateUseCase(date)
        ) { allFullTasks, allRecords ->
            if (allFullTasks.isNotEmpty()) {
                withContext(defaultDispatcher) {
                    allFullTasks.map { fullTaskModel ->
                        async {
                            val recordEntry =
                                allRecords.find { it.taskId == fullTaskModel.taskModel.id }?.entry
                            fullTaskModel.toFullTaskModelWithRecord(recordEntry)
                        }
                    }.awaitAll().sortTasks()
                }
            } else emptyList()
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        null
    )

    private val isLockedState = currentDateState.map { currentDate ->
        currentDate > todayDate
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false
    )

    override val uiScreenState: StateFlow<ViewScheduleScreenState>
        get() = TODO("Not yet implemented")

    override fun onEvent(event: ViewScheduleScreenEvent) {
        TODO("Not yet implemented")
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

    companion object {
        private const val FINISHED_WEIGHT = 1
        private const val PENDING_WEIGHT = 0
    }

}