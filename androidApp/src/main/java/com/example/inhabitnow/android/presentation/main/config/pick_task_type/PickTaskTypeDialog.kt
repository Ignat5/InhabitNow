package com.example.inhabitnow.android.presentation.main.config.pick_task_type

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.base.compose.BaseModalBottomSheetDialog
import com.example.inhabitnow.core.type.TaskType
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.enums.EnumEntries

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickTaskTypeDialog(
    allTaskTypes: List<TaskType>,
    onResult: (result: PickTaskTypeScreenResult) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    BaseModalBottomSheetDialog(
        sheetState = sheetState,
        dragHandle = null,
        onDismissRequest = { onResult(PickTaskTypeScreenResult.Dismiss) },
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(
                items = allTaskTypes,
                key = { it.ordinal }
            ) { taskType ->
                ItemTaskType(
                    taskType = taskType,
                    onClick = {
                        coroutineScope.launch {
                            sheetState.hide()
                            onResult(PickTaskTypeScreenResult.Confirm(taskType))
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ItemTaskType(
    taskType: TaskType,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconId = remember {
                taskType.toIconId()
            }
            Icon(
                painter = painterResource(iconId),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                val titleText = remember {
                    taskType.toTitleText()
                }
                Text(
                    text = titleText,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
                val descriptionText = remember {
                    taskType.toDescriptionText()
                }
                Text(
                    text = descriptionText,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private fun TaskType.toTitleText() = when (this) {
    TaskType.Habit -> "Habit"
    TaskType.RecurringTask -> "Recurring task"
    TaskType.SingleTask -> "Task"
}

private fun TaskType.toDescriptionText() = when(this) {
    TaskType.Habit -> "Activity performed over the period of time. Includes detailed progress tracking."
    TaskType.RecurringTask -> "Activity performed over the period of time. Dose not include progress tracking."
    TaskType.SingleTask -> "Activity performed on a single day. Does not include progress tracking."
}

private fun TaskType.toIconId() = when (this) {
    TaskType.Habit -> R.drawable.ic_habit
    TaskType.RecurringTask -> R.drawable.ic_recurring_task
    TaskType.SingleTask -> R.drawable.ic_task
}