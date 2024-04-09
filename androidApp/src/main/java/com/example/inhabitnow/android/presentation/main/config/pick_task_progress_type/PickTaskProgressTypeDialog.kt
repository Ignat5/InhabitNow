package com.example.inhabitnow.android.presentation.main.config.pick_task_progress_type

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder
import com.example.inhabitnow.core.type.TaskProgressType

@Composable
fun PickTaskProgressTypeDialog(
    allTaskProgressTypes: List<TaskProgressType>,
    onResult: (PickTaskProgressTypeScreenResult) -> Unit
) {
    BaseDialogBuilder.BaseDialog(
        onDismissRequest = { onResult(PickTaskProgressTypeScreenResult.Dismiss) },
        title = {
            BaseDialogBuilder.BaseDialogTitle(titleText = "Pick progress type")
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(
                items = allTaskProgressTypes,
                key = { it.ordinal }
            ) { taskProgressType ->
                ItemTaskProgressType(
                    taskProgressType = taskProgressType,
                    onClick = {
                        onResult(PickTaskProgressTypeScreenResult.Confirm(taskProgressType))
                    }
                )
            }
        }
    }
}

@Composable
private fun ItemTaskProgressType(
    taskProgressType: TaskProgressType,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp, vertical = 8.dp)
        ) {
            val iconId = remember {
                taskProgressType.toIconId()
            }
            Icon(
                painter = painterResource(iconId),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                val titleText = remember {
                    taskProgressType.toTitleText()
                }
                Text(
                    text = titleText,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                val descriptionText = remember {
                    taskProgressType.toDescriptionText()
                }
                Text(
                    text = descriptionText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun TaskProgressType.toTitleText() = when (this) {
    TaskProgressType.YesNo -> "Yes or No"
    TaskProgressType.Number -> "Number"
    TaskProgressType.Time -> "Time"
}

private fun TaskProgressType.toDescriptionText() = when (this) {
    TaskProgressType.YesNo -> "Daily record is either success of failure"
    TaskProgressType.Number -> "Daily record is a number value"
    TaskProgressType.Time -> "Daily record is a time value"
}

private fun TaskProgressType.toIconId() = when (this) {
    TaskProgressType.YesNo -> R.drawable.ic_progress_yes_no
    TaskProgressType.Number -> R.drawable.ic_progress_number
    TaskProgressType.Time -> R.drawable.ic_progress_time
}