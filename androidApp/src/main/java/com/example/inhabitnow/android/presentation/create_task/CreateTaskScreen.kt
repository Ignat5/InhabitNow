package com.example.inhabitnow.android.presentation.create_task

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.create_task.components.CreateTaskScreenEvent
import com.example.inhabitnow.android.presentation.create_task.components.CreateTaskScreenNavigation
import com.example.inhabitnow.android.presentation.create_task.components.CreateTaskScreenState
import com.example.inhabitnow.android.ui.toDayMonthYear
import com.example.inhabitnow.android.ui.toDisplay
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.domain.model.task.TaskWithContentModel
import com.example.inhabitnow.domain.model.task.content.TaskContentModel

@Composable
fun CreateTaskScreen(
    viewModel: CreateTaskViewModel,
    onNavigation: (CreateTaskScreenNavigation) -> Unit
) {
    BaseScreen(
        viewModel = viewModel,
        onNavigation = onNavigation,
        configContent = { config ->

        }
    ) { state, onEvent ->
        CreateTaskScreenStateless(state, onEvent)
    }
}

@Composable
private fun CreateTaskScreenStateless(
    state: CreateTaskScreenState,
    onEvent: (CreateTaskScreenEvent) -> Unit
) {
    Scaffold(
        topBar = {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(it)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                titleItem(
                    title = state.taskWithContentModel?.task?.title ?: "",
                    onClick = { /* TODO */ }
                )

                itemDivider()

                descriptionItem(
                    description = state.taskWithContentModel?.task?.description ?: "",
                    onClick = { /* TODO */ }
                )

                itemDivider()

                dailyGoalItem(
                    taskProgressContent = state.taskWithContentModel?.progressContent,
                    onClick = { /* TODO */ }
                )

                itemDivider()

                frequencyItem(
                    taskFrequencyContent = state.taskWithContentModel?.frequencyContent,
                    onClick = { /* TODO */ }
                )

                itemDivider()

                dateItems(
                    taskWithContentModel = state.taskWithContentModel,
                    onStartDateClick = { /* TODO */ },
                    onEndDateClick = { /* TODO */ },
                    onDateClick = { /* TODO */ }
                )

            }
        }
    }
}

private fun LazyListScope.titleItem(
    title: String,
    onClick: () -> Unit
) {
    item(
        key = ConfigItemType.Title,
        contentType = ConfigContentType.Standard
    ) {
        StandardItemConfig(
            iconResId = R.drawable.ic_edit,
            titleText = "Activity name",
            dataText = title,
            onClick = onClick
        )
    }
}

private fun LazyListScope.descriptionItem(
    description: String,
    onClick: () -> Unit
) {
    item(
        key = ConfigItemType.Description,
        contentType = ConfigContentType.Standard
    ) {
        StandardItemConfig(
            iconResId = R.drawable.ic_description,
            titleText = "Description",
            dataText = description,
            onClick = onClick
        )
    }
}

private fun LazyListScope.dailyGoalItem(
    taskProgressContent: TaskContentModel.ProgressContent?,
    onClick: () -> Unit
) {
    if (taskProgressContent == null) return
    item(
        key = ConfigItemType.DailyGoal,
        contentType = ConfigContentType.Standard
    ) {
        val dataText = remember(taskProgressContent) {
            when (taskProgressContent) {
                is TaskContentModel.ProgressContent.YesNo -> null
                is TaskContentModel.ProgressContent.Number -> taskProgressContent.toDisplay()
                is TaskContentModel.ProgressContent.Time -> taskProgressContent.toDisplay()
            }
        }
        StandardItemConfig(
            iconResId = R.drawable.ic_goal,
            titleText = "Daily goal",
            dataText = dataText ?: return@item,
            onClick = onClick
        )
    }
}

private fun LazyListScope.frequencyItem(
    taskFrequencyContent: TaskContentModel.FrequencyContent?,
    onClick: () -> Unit
) {
    if (taskFrequencyContent == null) return
    item(
        key = ConfigItemType.Frequency,
        contentType = ConfigContentType.Standard
    ) {
        val dataText = remember(taskFrequencyContent) {
            when (taskFrequencyContent) {
                is TaskContentModel.FrequencyContent.OneDay -> null
                is TaskContentModel.FrequencyContent.EveryDay -> taskFrequencyContent.toDisplay()
                is TaskContentModel.FrequencyContent.DaysOfWeek -> taskFrequencyContent.toDisplay()
            }
        }
        StandardItemConfig(
            iconResId = R.drawable.ic_frequency,
            titleText = "Frequency",
            dataText = dataText ?: return@item,
            onClick = onClick
        )
    }
}

private fun LazyListScope.dateItems(
    taskWithContentModel: TaskWithContentModel?,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    onDateClick: () -> Unit,
) {
    if (taskWithContentModel == null) return

    when (taskWithContentModel.task.type) {
        TaskType.SingleTask -> {
            item(
                key = ConfigItemType.Date,
                contentType = ConfigContentType.Standard
            ) {
                StandardItemConfig(
                    iconResId = R.drawable.ic_start_date,
                    titleText = "Date",
                    dataText = taskWithContentModel.task.startDate.toDayMonthYear(),
                    onClick = onDateClick
                )
            }
        }
        TaskType.RecurringTask, TaskType.Habit -> {
            item(
                key = ConfigItemType.StartDate,
                contentType = ConfigContentType.Standard
            ) {
                StandardItemConfig(
                    iconResId = R.drawable.ic_start_date,
                    titleText = "Start date",
                    dataText = taskWithContentModel.task.startDate.toDayMonthYear(),
                    onClick = onStartDateClick
                )
            }
        }
    }
}

@Composable
private fun StandardItemConfig(
    @DrawableRes iconResId: Int,
    titleText: String,
    dataText: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(iconResId),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
            Text(
                text = titleText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = dataText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun LazyListScope.itemDivider() {
    item(
        key = null,
        contentType = null
    ) {
        HorizontalDivider()
    }
}

private enum class ConfigItemType {
    Title,
    Description,
    DailyGoal,
    Frequency,
    Date,
    StartDate,
    EndDate
}

private enum class ConfigContentType {
    Standard,
    Switch
}