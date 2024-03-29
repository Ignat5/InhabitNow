package com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.base.compose.BaseModalBottomSheetDialog
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions.components.ViewHabitRecordActionsScreenEvent
import com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions.components.ViewHabitRecordActionsScreenResult
import com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions.components.ViewHabitRecordActionsScreenState
import com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions.model.ItemHabitRecordAction
import com.example.inhabitnow.android.presentation.view_schedule.model.TaskWithRecordModel
import com.example.inhabitnow.android.ui.limitNumberToString
import com.example.inhabitnow.android.ui.toDisplay
import com.example.inhabitnow.android.ui.toHourMinute
import com.example.inhabitnow.android.ui.toShortMonthDayYear
import com.example.inhabitnow.domain.model.record.content.RecordContentModel
import kotlinx.datetime.LocalDate

@Composable
fun ViewHabitRecordActionsDialog(
    stateHolder: ViewHabitRecordActionsStateHolder,
    onResult: (ViewHabitRecordActionsScreenResult) -> Unit
) {
    BaseScreen(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        ViewHabitRecordActionsDialogStateless(state, onEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ViewHabitRecordActionsDialogStateless(
    state: ViewHabitRecordActionsScreenState,
    onEvent: (ViewHabitRecordActionsScreenEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    BaseModalBottomSheetDialog(
        sheetState = sheetState,
        onDismissRequest = { onEvent(ViewHabitRecordActionsScreenEvent.OnDismissRequest) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TaskTitleBlock(
                taskWithRecord = state.taskWithRecord,
                date = state.date,
                onEditClick = {
                    onEvent(ViewHabitRecordActionsScreenEvent.OnEditClick)
                }
            )
            val onItemClick: (ItemHabitRecordAction) -> Unit = remember {
                val callback: (ItemHabitRecordAction) -> Unit = {
                    onEvent(ViewHabitRecordActionsScreenEvent.OnActionClick(it))
                }
                callback
            }
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(
                    items = state.allActionItems
                ) { item ->
                    val onClick = remember {
                        { onItemClick(item) }
                    }
                    when (item) {
                        is ItemHabitRecordAction.ContinuousProgress -> {
                            ItemProgress(
                                item = item,
                                onClick = onClick
                            )
                        }

                        is ItemHabitRecordAction.Done -> {
                            ItemDone(onClick = onClick)
                        }

                        is ItemHabitRecordAction.Fail -> {
                            ItemFail(onClick = onClick)
                        }

                        is ItemHabitRecordAction.Skip -> {
                            ItemSkip(onClick = onClick)
                        }

                        is ItemHabitRecordAction.Reset -> {
                            ItemReset(onClick = onClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemDone(onClick: () -> Unit) {
    BaseTitleItem(
        iconResId = R.drawable.ic_task,
        title = "Mark as done",
        onClick = onClick
    )
}

@Composable
private fun ItemFail(onClick: () -> Unit) {
    BaseTitleItem(
        iconResId = R.drawable.ic_failure,
        title = "Mark as failure",
        onClick = onClick
    )
}

@Composable
private fun ItemSkip(onClick: () -> Unit) {
    BaseTitleItem(
        iconResId = R.drawable.ic_skip,
        title = "Skip",
        onClick = onClick
    )
}

@Composable
private fun ItemReset(onClick: () -> Unit) {
    BaseTitleItem(
        iconResId = R.drawable.ic_reset,
        title = "Reset entry",
        onClick = onClick
    )
}

@Composable
private fun ItemProgress(
    item: ItemHabitRecordAction.ContinuousProgress,
    onClick: () -> Unit
) {
    BaseItemContainer(
        iconResId = R.drawable.ic_goal,
        onClick = onClick
    ) {
        val title = remember(item) {
            when (item) {
                is ItemHabitRecordAction.ContinuousProgress.Number -> {
                    val recordNumber =
                        (item.taskWithRecord.recordEntry as? RecordContentModel.Entry.Number)
                            ?.number?.limitNumberToString()
                    val limitUnit = item.taskWithRecord.task.progressContent.limitUnit
                    val suffix = recordNumber?.let {
                        "$recordNumber $limitUnit"
                    } ?: item.taskWithRecord.statusType.toDisplay()
                    "Progress: $suffix"
                }

                is ItemHabitRecordAction.ContinuousProgress.Time -> {
                    val recordTime =
                        (item.taskWithRecord.recordEntry as? RecordContentModel.Entry.Time)
                            ?.time
                    val suffix = recordTime?.let {
                        recordTime.toHourMinute()
                    } ?: item.taskWithRecord.statusType.toDisplay()
                    "Progress: $suffix"
                }
            }
        }
        val subtitle = remember(item) {
            when (item) {
                is ItemHabitRecordAction.ContinuousProgress.Number -> {
                    val pc = item.taskWithRecord.task.progressContent
                    val limitType = pc.limitType.toDisplay()
                    val limitNumber = pc.limitNumber.limitNumberToString()
                    "Goal: $limitType $limitNumber ${pc.limitUnit}"
                }

                is ItemHabitRecordAction.ContinuousProgress.Time -> {
                    val pc = item.taskWithRecord.task.progressContent
                    val limitType = pc.limitType.toDisplay()
                    val limitTime = pc.limitTime.toHourMinute()
                    "Goal: $limitType $limitTime"
                }
            }
        }
        BaseItemProgressContent(
            title = title,
            subtitle = subtitle
        )
    }
}

@Composable
private fun BaseTitleItem(
    @DrawableRes iconResId: Int,
    title: String,
    onClick: () -> Unit
) {
    BaseItemContainer(
        iconResId = iconResId,
        onClick = onClick,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun BaseItemProgressContent(
    title: String,
    subtitle: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun BaseItemContainer(
    @DrawableRes iconResId: Int,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                content()
            }
        }
    }
}

@Composable
private fun TaskTitleBlock(
    taskWithRecord: TaskWithRecordModel.Habit,
    date: LocalDate,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = taskWithRecord.task.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
//                val statusText = remember { taskWithRecord.statusType.toDisplay() }
//                Text(
//                    text = statusText,
//                    style = MaterialTheme.typography.labelMedium,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                    modifier = Modifier.weight(1f),
//                    textAlign = TextAlign.Start,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
            }
            Text(
                text = date.toShortMonthDayYear(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(onClick = onEditClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        }
    }
}