package com.example.inhabitnow.android.presentation.view_schedule

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenConfig
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenEvent
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenNavigation
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenState
import com.example.inhabitnow.android.presentation.view_schedule.model.FullTaskScheduleModel
import com.example.inhabitnow.android.presentation.view_schedule.model.TaskScheduleStatus
import com.example.inhabitnow.core.type.ProgressLimitType
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.domain.model.record.RecordModel
import com.example.inhabitnow.domain.model.record.content.RecordContentModel
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
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

//    private val allTasksState = currentDateState.flatMapLatest { date ->
//        combine(
//            readFullTasksByDateUseCase(date),
//            readRecordsByDateUseCase(date)
//        ) { allFullTasks, allRecords ->
//            if (allFullTasks.isNotEmpty()) {
//                withContext(defaultDispatcher) {
//                    allFullTasks.map { fullTaskModel ->
//                        async {
//                            val record =
//                                allRecords.find { it.taskId == fullTaskModel.taskModel.task.id }
//                            FullTaskScheduleModel(
//                                fullTaskModel = fullTaskModel,
//                                status = fullTaskModel.taskModel.figureStatus(
//                                    record = record
//                                )
//                            )
//                        }
//                    }.awaitAll()
//                }
//            } else emptyList()
//        }
//    }

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

//    private fun TaskWithContentModel.figureStatus(
//        record: RecordModel?,
//    ): TaskScheduleStatus = this.let { taskWithContentModel ->
//        when (taskWithContentModel.task.type) {
//            TaskType.Habit -> {
//                when (val pc = taskWithContentModel.progressContent) {
//                    is TaskContentModel.ProgressContent.YesNo -> {
//                        TaskScheduleStatus.Habit.YesNo(
//                            statusType = when (record?.entry) {
//                                is RecordContentModel.Entry.Done -> TaskScheduleStatus.StatusType.Done
//                                is RecordContentModel.Entry.Skip -> TaskScheduleStatus.StatusType.Skipped
//                                is RecordContentModel.Entry.Fail -> TaskScheduleStatus.StatusType.Failed
//                                else -> TaskScheduleStatus.StatusType.Pending
//                            }
//                        )
//                    }
//
//                    is TaskContentModel.ProgressContent.Number -> {
//                        TaskScheduleStatus.Habit.Continuous.Number(
//                            statusType = when (val entry = record?.entry) {
//                                is RecordContentModel.Entry.Number -> {
//                                    val limitNumber = pc.limitNumber.toDoubleOrNull() ?: 0.0
//                                    val entryNumber = entry.number.toDoubleOrNull() ?: 0.0
//                                    val isDone = when (pc.limitType) {
//                                        ProgressLimitType.AtLeast -> entryNumber >= limitNumber
//                                        ProgressLimitType.Exactly -> entryNumber == limitNumber
//                                    }
//                                    if (isDone) TaskScheduleStatus.StatusType.Done
//                                    else TaskScheduleStatus.StatusType.InProgress
//                                }
//
//                                is RecordContentModel.Entry.Skip -> TaskScheduleStatus.StatusType.Skipped
//                                is RecordContentModel.Entry.Fail -> TaskScheduleStatus.StatusType.Failed
//                                else -> TaskScheduleStatus.StatusType.Pending
//                            },
//                            progressContent = pc,
//                            record = record?.entry as? RecordContentModel.Entry.Number
//                        )
//                    }
//
//                    is TaskContentModel.ProgressContent.Time -> {
//                        TaskScheduleStatus.Habit.Continuous.Time(
//                            statusType = when (val entry = record?.entry) {
//                                is RecordContentModel.Entry.Time -> {
//                                    val limitTime = pc.limitTime
//                                    val entryTime = entry.time
//                                    val isDone = when (pc.limitType) {
//                                        ProgressLimitType.AtLeast -> entryTime >= limitTime
//                                        ProgressLimitType.Exactly -> entryTime == limitTime
//                                    }
//                                    if (isDone) TaskScheduleStatus.StatusType.Done
//                                    else TaskScheduleStatus.StatusType.InProgress
//                                }
//
//                                is RecordContentModel.Entry.Skip -> TaskScheduleStatus.StatusType.Skipped
//                                is RecordContentModel.Entry.Fail -> TaskScheduleStatus.StatusType.Failed
//                                else -> TaskScheduleStatus.StatusType.Pending
//                            },
//                            progressContent = pc,
//                            record = record?.entry as? RecordContentModel.Entry.Time
//                        )
//                    }
//                }
//            }
//
//            TaskType.RecurringTask, TaskType.SingleTask -> {
//                TaskScheduleStatus.Task(
//                    statusType = when (record?.entry) {
//                        is RecordContentModel.Entry.Done -> TaskScheduleStatus.StatusType.Done
//                        else -> TaskScheduleStatus.StatusType.Pending
//                    }
//                )
//            }
//        }
//    }


}