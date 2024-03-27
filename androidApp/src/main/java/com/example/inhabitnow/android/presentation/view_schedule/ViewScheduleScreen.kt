package com.example.inhabitnow.android.presentation.view_schedule

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenEvent
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenNavigation
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenState
import com.example.inhabitnow.android.presentation.view_schedule.model.FullTaskWithRecordModel
import com.example.inhabitnow.android.presentation.view_schedule.model.TaskScheduleStatusType
import com.example.inhabitnow.android.presentation.view_schedule.model.TaskWithRecordModel
import com.example.inhabitnow.android.ui.toDisplay
import com.example.inhabitnow.android.ui.toHourMinute
import com.example.inhabitnow.core.type.ProgressLimitType
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.domain.model.record.content.RecordContentModel
import com.example.inhabitnow.domain.model.reminder.ReminderModel
import com.example.inhabitnow.domain.model.tag.TagModel
import com.example.inhabitnow.domain.model.task.TaskModel

private const val PROGRESS_FULL = 1f
private const val PROGRESS_EMPTY = 0f

@Composable
fun ViewScheduleScreen(
    onNavigate: (ViewScheduleScreenNavigation) -> Unit
) {
    val viewModel: ViewScheduleViewModel = hiltViewModel()
    BaseScreen(
        viewModel = viewModel,
        onNavigation = onNavigate,
        configContent = { config, onEvent ->

        }
    ) { state, onEvent ->
        ViewScheduleScreenStateless(state, onEvent)
    }
}

@Composable
private fun ViewScheduleScreenStateless(
    state: ViewScheduleScreenState,
    onEvent: (ViewScheduleScreenEvent) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(
                    items = state.allTasksWithRecord,
                    key = { it.taskWithRecordModel.task.id }
                ) { item ->
                    ItemTaskWithRecord(
                        item = item,
                        onClick = {},
                        onLongClick = {}
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ItemTaskWithRecord(
    item: FullTaskWithRecordModel,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Box(
        modifier = Modifier
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
            ProgressIndicator(taskWithRecord = item.taskWithRecordModel)
        }
    }
}

@Composable
private fun ProgressIndicator(taskWithRecord: TaskWithRecordModel) {
    val progress: Float = when (taskWithRecord.statusType) {
        is TaskScheduleStatusType.Pending -> PROGRESS_EMPTY
        is TaskScheduleStatusType.Done -> PROGRESS_FULL
        is TaskScheduleStatusType.Failed -> PROGRESS_FULL
        is TaskScheduleStatusType.Skipped -> PROGRESS_FULL
        is TaskScheduleStatusType.Locked -> PROGRESS_EMPTY

        is TaskScheduleStatusType.InProgress -> {
            when (taskWithRecord) {
                is TaskWithRecordModel.Habit.HabitContinuous -> {
                    when (taskWithRecord) {
                        is TaskWithRecordModel.Habit.HabitContinuous.HabitNumber -> {
                            when (val entry = taskWithRecord.recordEntry) {
                                is RecordContentModel.Entry.Number -> {
                                    val progressContent = taskWithRecord.task.progressContent
                                    (entry.number / progressContent.limitNumber).toFloat()
                                }

                                else -> 0f
                            }
                        }

                        is TaskWithRecordModel.Habit.HabitContinuous.HabitTime -> {
                            when (val entry = taskWithRecord.recordEntry) {
                                is RecordContentModel.Entry.Time -> {
                                    val progressContent = taskWithRecord.task.progressContent
                                    (entry.time.toSecondOfDay() / progressContent.limitTime.toSecondOfDay())
                                        .toFloat()
                                }

                                else -> 0f
                            }
                        }
                    }
                }

                else -> 0f
            }
        }
    }

    val containerColor = when (taskWithRecord.statusType) {
        is TaskScheduleStatusType.Skipped -> MaterialTheme.colorScheme.surfaceContainerHigh
        is TaskScheduleStatusType.Failed -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    val progressColor = when (taskWithRecord.statusType) {
        is TaskScheduleStatusType.Skipped -> MaterialTheme.colorScheme.onSurface
        is TaskScheduleStatusType.Failed -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onPrimaryContainer
    }
    val progressState by animateFloatAsState(targetValue = progress, label = "")
    if (taskWithRecord.statusType != TaskScheduleStatusType.Locked) {
        CircularProgressIndicator(
            progress = {
                progressState
            },
            modifier = Modifier.size(32.dp),
            color = progressColor,
            trackColor = containerColor
        )
    }
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
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ItemTaskType(fullTaskWithRecord.taskWithRecordModel.task.type)
        if (fullTaskWithRecord.allReminders.isNotEmpty()) {
            ItemTaskReminders(fullTaskWithRecord.allReminders)
        }
        if (fullTaskWithRecord.allTags.isNotEmpty()) {
            ItemTaskTags(fullTaskWithRecord.allTags)
        }
    }
}

@Composable
private fun ItemTaskTags(allTags: List<TagModel>) {
    ItemDetailContainer {
        val text = remember(allTags) {
            buildAnnotatedString {
                allTags.forEachIndexed { index, tag ->
                    append(tag.title)
                    if (index != allTags.lastIndex) {
                        append(" | ")
                    }
                }
            }
        }
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ItemTaskReminders(allReminders: List<ReminderModel>) {
    val text = remember(allReminders) {
        buildAnnotatedString {
            allReminders.forEachIndexed { index, reminder ->
                append(reminder.time.toHourMinute())
                if (index != allReminders.lastIndex) {
                    append(" | ")
                }
            }
        }
    }
    ItemDetailContainer {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ItemTaskType(taskType: TaskType) {
    ItemDetailContainer {
        Text(text = taskType.toDisplay())
    }
}

@Composable
private fun ItemDetailContainer(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = MaterialTheme.shapes.extraSmall
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            ProvideContentColorTextStyle(
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                textStyle = MaterialTheme.typography.labelMedium
            ) {
                content()
            }
        }
    }
}

private fun getProgressTextOrNull(taskWithRecord: TaskWithRecordModel): String? {
    return when (taskWithRecord.statusType) {
        is TaskScheduleStatusType.InProgress -> {
            when (taskWithRecord) {
                is TaskWithRecordModel.Habit.HabitContinuous.HabitNumber -> {
                    val progressContent = taskWithRecord.task.progressContent
                    when (val entry = taskWithRecord.recordEntry) {
                        is RecordContentModel.Entry.Number -> {
                            "${entry.number}/${progressContent.limitNumber} ${progressContent.limitUnit}"
                        }

                        else -> null
                    }
                }

                is TaskWithRecordModel.Habit.HabitContinuous.HabitTime -> {
                    val progressContent = taskWithRecord.task.progressContent
                    when (val entry = taskWithRecord.recordEntry) {
                        is RecordContentModel.Entry.Time -> {
                            "${entry.time.toHourMinute()}/${progressContent.limitTime.toHourMinute()}"
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
//    return when (taskWithRecord) {
//        is TaskWithRecordModel.Habit -> {
//            when (taskWithRecord) {
//                is TaskWithRecordModel.Habit.HabitContinuous -> {
//                    when (taskWithRecord) {
//                        is TaskWithRecordModel.Habit.HabitContinuous.HabitNumber -> {
//                            when (val entry = taskWithRecord.recordEntry) {
//                                null -> null
//                                is RecordContentModel.Entry.Number -> {
//                                    "${entry.number}/${taskWithRecord.task.progressContent.limitNumber} ${taskWithRecord.task.progressContent.limitUnit}"
//                                }
//
//                                is RecordContentModel.Entry.Skip -> getSkippedText()
//                                is RecordContentModel.Entry.Fail -> getFailedText()
//                            }
//                        }
//
//                        is TaskWithRecordModel.Habit.HabitContinuous.HabitTime -> {
//                            when (val entry = taskWithRecord.recordEntry) {
//                                null -> null
//                                is RecordContentModel.Entry.Time -> {
//                                    "${entry.time.toHourMinute()}/${taskWithRecord.task.progressContent.limitTime.toHourMinute()}"
//                                }
//
//                                is RecordContentModel.Entry.Skip -> getSkippedText()
//                                is RecordContentModel.Entry.Fail -> getFailedText()
//                            }
//                        }
//                    }
//                }
//
//                is TaskWithRecordModel.Habit.HabitYesNo -> {
//                    when (val entry = taskWithRecord.recordEntry) {
//                        null -> null
//                        is RecordContentModel.Entry.Done -> null
//                        is RecordContentModel.Entry.Skip -> getSkippedText()
//                        is RecordContentModel.Entry.Fail -> getFailedText()
//                    }
//                }
//            }
//        }
//
//        else -> null
//    }
}

@Composable
private fun ProvideContentColorTextStyle(
    contentColor: Color,
    textStyle: TextStyle,
    content: @Composable () -> Unit
) {
    val mergedStyle = LocalTextStyle.current.merge(textStyle)
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides mergedStyle,
        content = content
    )
}