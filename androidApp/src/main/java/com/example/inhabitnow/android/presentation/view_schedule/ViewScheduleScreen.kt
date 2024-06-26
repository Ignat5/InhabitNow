package com.example.inhabitnow.android.presentation.view_schedule

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.common.pick_date.PickDateDialog
import com.example.inhabitnow.android.presentation.main.config.pick_task_progress_type.PickTaskProgressTypeDialog
import com.example.inhabitnow.android.presentation.main.config.pick_task_type.PickTaskTypeDialog
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenConfig
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenEvent
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenNavigation
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenState
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_number_record.EnterTaskNumberRecordDialog
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_time_record.EnterTaskTimeRecordDialog
import com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions.ViewHabitRecordActionsDialog
import com.example.inhabitnow.android.presentation.view_schedule.model.FullTaskWithRecordModel
import com.example.inhabitnow.android.presentation.view_schedule.model.ItemDayOfWeek
import com.example.inhabitnow.android.presentation.view_schedule.model.TaskScheduleStatusType
import com.example.inhabitnow.android.presentation.view_schedule.model.TaskWithRecordModel
import com.example.inhabitnow.android.ui.base.BaseTaskItemBuilder
import com.example.inhabitnow.android.ui.base.BaseCommonComponents
import com.example.inhabitnow.android.ui.limitNumberToString
import com.example.inhabitnow.android.ui.toDisplay
import com.example.inhabitnow.android.ui.toHourMinute
import com.example.inhabitnow.android.ui.toShortMonthDayYear
import com.example.inhabitnow.core.type.ProgressLimitType
import com.example.inhabitnow.domain.model.record.content.RecordContentModel
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

private const val PROGRESS_FULL = 1f
private const val PROGRESS_EMPTY = 0f

@Composable
fun ViewScheduleScreen(
    onMenuClick: () -> Unit,
    onNavigate: (ViewScheduleScreenNavigation) -> Unit
) {
    val viewModel: ViewScheduleViewModel = hiltViewModel()
    BaseScreen(
        viewModel = viewModel,
        onNavigation = onNavigate,
        configContent = { config, onEvent ->
            ViewScheduleConfigStateless(config = config) {
                onEvent(it)
            }
        }
    ) { state, onEvent ->
        ViewScheduleScreenStateless(onMenuClick, state, onEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ViewScheduleScreenStateless(
    onMenuClick: () -> Unit,
    state: ViewScheduleScreenState,
    onEvent: (ViewScheduleScreenEvent) -> Unit
) {
    Scaffold(
        topBar = {
            ScreenTopAppBar(
                currentDate = state.currentDate,
                onMenuClick = onMenuClick,
                onSearchClick = {
                    onEvent(ViewScheduleScreenEvent.OnSearchClick)
                },
                onPickDateClick = {
                    onEvent(ViewScheduleScreenEvent.OnPickDateClick)
                }
            )
        },
        floatingActionButton = {
            BaseCommonComponents.CreateTaskFAB(
                onClick = {
                    onEvent(ViewScheduleScreenEvent.OnCreateTaskClick)
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (state.allTasksWithRecord is UIResultModel.NoData) {
                Text(
                    text = "No activities scheduled for the day",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
            Column(modifier = Modifier.fillMaxSize()) {
                WeekRow(
                    startOfWeekDate = state.startOfWeekDate,
                    currentDate = state.currentDate,
                    todayDate = state.todayDate,
                    onDateClick = {
                        onEvent(ViewScheduleScreenEvent.OnDateClick(it))
                    },
                    onPrevClick = {
                        onEvent(ViewScheduleScreenEvent.OnPrevWeekClick)
                    },
                    onNextClick = {
                        onEvent(ViewScheduleScreenEvent.OnNextWeekClick)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    when (state.allTasksWithRecord) {
                        is UIResultModel.Loading, is UIResultModel.Data -> {
                            val items = state.allTasksWithRecord.data ?: emptyList()
                            itemsIndexed(
                                items = items,
                                key = { _, item -> item.taskWithRecordModel.task.id }
                            ) { index, item ->
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    ItemTaskWithRecord(
                                        item = item,
                                        isLocked = state.isLocked,
                                        onClick = {
                                            onEvent(ViewScheduleScreenEvent.OnTaskClick(item.taskWithRecordModel.task.id))
                                        },
                                        onLongClick = {
                                            onEvent(ViewScheduleScreenEvent.OnTaskLongClick(item.taskWithRecordModel.task.id))
                                        },
                                        modifier = Modifier.animateItemPlacement()
                                    )
                                    if (index != items.lastIndex) {
                                        HorizontalDivider(modifier = Modifier.alpha(0.2f))
                                    }
                                }
                            }
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ItemTaskWithRecord(
    item: FullTaskWithRecordModel,
    isLocked: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TitleRow(taskWithRecord = item.taskWithRecordModel)
                DetailRow(fullTaskWithRecord = item)
            }
//            AnimatedVisibility(visible = item.taskWithRecordModel.statusType != TaskScheduleStatusType.Locked) {
            ProgressIndicator(taskWithRecord = item.taskWithRecordModel)
//            }
        }
    }
}

@Composable
private fun ProgressIndicator(taskWithRecord: TaskWithRecordModel) {
    val progress: Float = when (taskWithRecord.statusType) {
        is TaskScheduleStatusType.Locked -> PROGRESS_EMPTY
        is TaskScheduleStatusType.Pending -> PROGRESS_EMPTY
        is TaskScheduleStatusType.Done -> PROGRESS_FULL
        is TaskScheduleStatusType.Failed -> PROGRESS_FULL
        is TaskScheduleStatusType.Skipped -> PROGRESS_FULL

        is TaskScheduleStatusType.InProgress -> {
            when (taskWithRecord) {
                is TaskWithRecordModel.Habit.HabitContinuous -> {
                    when (taskWithRecord) {
                        is TaskWithRecordModel.Habit.HabitContinuous.HabitNumber -> {
                            when (val entry = taskWithRecord.recordEntry) {
                                is RecordContentModel.Entry.Number -> {
                                    val progressContent = taskWithRecord.task.progressContent
                                    when (progressContent.limitType) {
                                        ProgressLimitType.AtLeast -> {
                                            (entry.number / progressContent.limitNumber).toFloat()
                                        }

                                        ProgressLimitType.Exactly -> {
                                            if (entry.number == progressContent.limitNumber) PROGRESS_FULL
                                            else PROGRESS_EMPTY
                                        }

                                        ProgressLimitType.NoMoreThan -> {
                                            if (entry.number <= progressContent.limitNumber) PROGRESS_FULL
                                            else PROGRESS_EMPTY
                                        }
                                    }
                                }

                                else -> PROGRESS_EMPTY
                            }
                        }

                        is TaskWithRecordModel.Habit.HabitContinuous.HabitTime -> {
                            when (val entry = taskWithRecord.recordEntry) {
                                is RecordContentModel.Entry.Time -> {
                                    val pc = taskWithRecord.task.progressContent
                                    when (pc.limitType) {
                                        ProgressLimitType.AtLeast -> {
                                            entry.time.toSecondOfDay().toFloat()
                                                .let { entrySeconds ->
                                                    pc.limitTime.toSecondOfDay().toFloat()
                                                        .let { limitSeconds ->
                                                            (entrySeconds / limitSeconds).toFloat()
                                                        }
                                                }
                                        }

                                        ProgressLimitType.Exactly -> {
                                            entry.time.toSecondOfDay().let { entrySeconds ->
                                                pc.limitTime.toSecondOfDay().let { limitSeconds ->
                                                    if (entrySeconds == limitSeconds) PROGRESS_FULL
                                                    else PROGRESS_EMPTY
                                                }
                                            }
                                        }

                                        ProgressLimitType.NoMoreThan -> {
                                            entry.time.toSecondOfDay().let { entrySeconds ->
                                                pc.limitTime.toSecondOfDay().let { limitSeconds ->
                                                    if (entrySeconds <= limitSeconds) PROGRESS_FULL
                                                    else PROGRESS_EMPTY
                                                }
                                            }
                                        }
                                    }
                                }

                                else -> PROGRESS_EMPTY
                            }
                        }
                    }
                }

                else -> PROGRESS_EMPTY
            }
        }
    }
    val containerColor = when (taskWithRecord.statusType) {
        is TaskScheduleStatusType.Locked -> MaterialTheme.colorScheme.surfaceContainerLow
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    val progressColor = when (taskWithRecord.statusType) {
        is TaskScheduleStatusType.Skipped -> MaterialTheme.colorScheme.surfaceContainerHigh
        is TaskScheduleStatusType.Failed -> MaterialTheme.colorScheme.errorContainer
        is TaskScheduleStatusType.Locked -> MaterialTheme.colorScheme.surfaceContainerLow
        else -> MaterialTheme.colorScheme.onPrimaryContainer
    }
    val progressState by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(stiffness = Spring.StiffnessVeryLow),
        label = ""
    )
    CircularProgressIndicator(
        progress = {
            progressState
        },
        modifier = Modifier.size(32.dp),
        color = progressColor,
        trackColor = containerColor,
        strokeWidth = 2.dp,
    )
}

@Composable
private fun TitleRow(taskWithRecord: TaskWithRecordModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = taskWithRecord.task.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        val progressText = remember(taskWithRecord) {
            getProgressTextOrNull(taskWithRecord)
        }
        if (progressText != null) {
            Text(
                text = progressText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun DetailRow(fullTaskWithRecord: FullTaskWithRecordModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        BaseTaskItemBuilder.ChipTaskType(taskType = fullTaskWithRecord.taskWithRecordModel.task.type)
        BaseTaskItemBuilder.ChipTaskProgressType(taskProgressType = fullTaskWithRecord.taskWithRecordModel.task.progressType)
        BaseTaskItemBuilder.ChipTaskPriority(priority = fullTaskWithRecord.taskWithRecordModel.task.priority)
        if (fullTaskWithRecord.allReminders.isNotEmpty()) {
            BaseTaskItemBuilder.ChipTaskReminders(allReminders = fullTaskWithRecord.allReminders)
        }
    }
}

private fun getProgressTextOrNull(taskWithRecord: TaskWithRecordModel): String? {
    return when (taskWithRecord.statusType) {
        is TaskScheduleStatusType.Done, is TaskScheduleStatusType.InProgress -> {
            when (taskWithRecord) {
                is TaskWithRecordModel.Habit.HabitContinuous.HabitNumber -> {
                    val progressContent = taskWithRecord.task.progressContent
                    when (val entry = taskWithRecord.recordEntry) {
                        is RecordContentModel.Entry.Number -> {
                            "${entry.number.limitNumberToString()} ${progressContent.limitUnit}"
                        }

                        else -> null
                    }
                }

                is TaskWithRecordModel.Habit.HabitContinuous.HabitTime -> {
                    when (val entry = taskWithRecord.recordEntry) {
                        is RecordContentModel.Entry.Time -> {
                            entry.time.toHourMinute()
                        }

                        else -> null
                    }
                }

                else -> null
            }
        }

        is TaskScheduleStatusType.Skipped -> "skipped"
        is TaskScheduleStatusType.Failed -> "failed"
        else -> null
    }
}

@Composable
private fun WeekRow(
    startOfWeekDate: LocalDate,
    currentDate: LocalDate,
    todayDate: LocalDate,
    onDateClick: (LocalDate) -> Unit,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val weekDaysCount = remember { DayOfWeek.entries.size }
    Row(modifier = Modifier.fillMaxWidth()) {
        NextPrevButton(
            iconId = R.drawable.ic_previous,
            onClick = onPrevClick
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(weekDaysCount),
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            items(weekDaysCount) { offset ->
                val itemDate = remember(startOfWeekDate) {
                    startOfWeekDate.plus(offset, DateTimeUnit.DAY)
                }
                ItemWeekDay(
                    item = when (itemDate) {
                        currentDate -> ItemDayOfWeek.Current(itemDate)
                        todayDate -> ItemDayOfWeek.Today(itemDate)
                        else -> ItemDayOfWeek.Day(itemDate)
                    },
                    onClick = {
                        onDateClick(itemDate)
                    }
                )
            }
        }
        NextPrevButton(
            iconId = R.drawable.ic_next,
            onClick = onNextClick
        )
    }
}

@Composable
private fun ItemWeekDay(
    item: ItemDayOfWeek,
    onClick: () -> Unit
) {
    val dayOfWeekText = remember(item) {
        item.date.dayOfWeek.toDisplay().take(3)
    }
    val dayOfMonthText = remember(item) {
        "${item.date.dayOfMonth}"
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp)
            .clickable { onClick() }
            .then(
                when (item) {
                    is ItemDayOfWeek.Current -> {
                        Modifier
                            .background(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.shapes.small
                            )
                    }

                    is ItemDayOfWeek.Today -> {
                        Modifier
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.shapes.small
                            )
                    }

                    is ItemDayOfWeek.Day -> Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(4.dp)
        ) {
            Text(
                text = dayOfWeekText,
                style = MaterialTheme.typography.labelSmall,
                color = when (item) {
                    is ItemDayOfWeek.Current -> MaterialTheme.colorScheme.onPrimary
                    is ItemDayOfWeek.Today -> MaterialTheme.colorScheme.primary
                    is ItemDayOfWeek.Day -> MaterialTheme.colorScheme.onSurface
                }
            )
            Text(
                text = dayOfMonthText,
                style = MaterialTheme.typography.titleSmall,
                color = when (item) {
                    is ItemDayOfWeek.Current -> MaterialTheme.colorScheme.onPrimary
                    is ItemDayOfWeek.Today -> MaterialTheme.colorScheme.primary
                    is ItemDayOfWeek.Day -> MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@Composable
private fun NextPrevButton(
    @DrawableRes iconId: Int,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = iconId),
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = null
        )
    }
}

@Composable
private fun ViewScheduleConfigStateless(
    config: ViewScheduleScreenConfig,
    onResultEvent: (ViewScheduleScreenEvent.ResultEvent) -> Unit
) {
    when (config) {
        is ViewScheduleScreenConfig.PickDate -> {
            PickDateDialog(stateHolder = config.stateHolder) {
                onResultEvent(ViewScheduleScreenEvent.ResultEvent.PickDate(it))
            }
        }

        is ViewScheduleScreenConfig.EnterTaskNumberRecord -> {
            EnterTaskNumberRecordDialog(stateHolder = config.stateHolder) {
                onResultEvent(ViewScheduleScreenEvent.ResultEvent.EnterTaskNumberRecord(it))
            }
        }

        is ViewScheduleScreenConfig.EnterTaskTimeRecord -> {
            EnterTaskTimeRecordDialog(stateHolder = config.stateHolder) {
                onResultEvent(ViewScheduleScreenEvent.ResultEvent.EnterTaskTimeRecord(it))
            }
        }

        is ViewScheduleScreenConfig.ViewHabitRecordActions -> {
            ViewHabitRecordActionsDialog(stateHolder = config.stateHolder) {
                onResultEvent(ViewScheduleScreenEvent.ResultEvent.ViewHabitRecordActions(it))
            }
        }

        is ViewScheduleScreenConfig.PickTaskType -> {
            PickTaskTypeDialog(allTaskTypes = config.allTaskTypes) {
                onResultEvent(ViewScheduleScreenEvent.ResultEvent.PickTaskType(it))
            }
        }

        is ViewScheduleScreenConfig.PickTaskProgressType -> {
            PickTaskProgressTypeDialog(allTaskProgressTypes = config.allTaskProgressTypes) {
                onResultEvent(ViewScheduleScreenEvent.ResultEvent.PickTaskProgressType(it))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenTopAppBar(
    currentDate: LocalDate,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onPickDateClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = currentDate.toShortMonthDayYear())
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(painter = painterResource(id = R.drawable.ic_menu), contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null
                )
            }
            IconButton(onClick = onPickDateClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_pick_date),
                    contentDescription = null
                )
            }
        }
    )
}