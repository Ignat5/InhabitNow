package com.example.inhabitnow.android.presentation.create_edit_task.create

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.PickTaskTitleDialog
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.components.PickTaskTitleScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.number.PickTaskNumberProgressDialog
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.number.components.PickTaskNumberProgressScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.time.PickTaskTimeProgressDialog
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenConfig
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenNavigation
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenState
import com.example.inhabitnow.android.presentation.model.UITaskContent
import com.example.inhabitnow.android.ui.toDayMonthYear
import com.example.inhabitnow.android.ui.toDisplay
import com.example.inhabitnow.domain.model.task.content.TaskContentModel

@Composable
fun CreateTaskScreen(onNavigation: (CreateTaskScreenNavigation) -> Unit) {
    val viewModel: CreateTaskViewModel = hiltViewModel()
    BaseScreen(
        viewModel = viewModel,
        onNavigation = onNavigation,
        configContent = { config ->
            CreateTaskScreenConfigStateless(
                config = config,
                onResultEvent = { resultEvent ->
                    viewModel.onEvent(resultEvent)
                }
            )
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
    BackHandler { onEvent(CreateTaskScreenEvent.OnDismissRequest) }
    Scaffold(
        topBar = {
            ScreenTopBar(
                canSave = state.canSave,
                onSaveClick = { onEvent(CreateTaskScreenEvent.OnSaveClick) },
                onCloseClick = { onEvent(CreateTaskScreenEvent.OnDismissRequest) }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                run {
                    titleItem(
                        title = state.taskTitle,
                        onClick = {
                            onEvent(CreateTaskScreenEvent.ConfigEvent.OnConfigTaskTitleClick)
                        }
                    )

                    itemDivider()
                }

                run {
                    descriptionItem(
                        description = state.taskDescription,
                        onClick = { /* TODO */ }
                    )

                    itemDivider()
                }

                run {
                    when (state.taskProgressContent) {
                        is UITaskContent.Progress.Number -> {
                            numberProgressItem(
                                taskProgressContent = state.taskProgressContent.progressContent,
                                onClick = {
                                    onEvent(CreateTaskScreenEvent.ConfigEvent.OnConfigTaskNumberProgressClick)
                                }
                            )
                            itemDivider()
                        }

                        is UITaskContent.Progress.Time -> {
                            timeProgressItem(
                                taskProgressContent = state.taskProgressContent.progressContent,
                                onClick = {
                                    onEvent(CreateTaskScreenEvent.ConfigEvent.OnConfigTaskTimeProgressClick)
                                }
                            )
                            itemDivider()
                        }

                        else -> Unit
                    }

                }

                run {
                    when (state.taskFrequencyContent) {
                        is UITaskContent.Frequency.EveryDay -> {
                            frequencyItem(
                                frequencyContent = state.taskFrequencyContent.frequencyContent,
                                onClick = {}
                            )
                            itemDivider()
                        }

                        is UITaskContent.Frequency.DaysOfWeek -> {
                            frequencyItem(
                                frequencyContent = state.taskFrequencyContent.frequencyContent,
                                onClick = {}
                            )
                            itemDivider()
                        }

                        else -> Unit
                    }
                }

                run {
                    when (state.taskDateContent) {
                        is UITaskContent.Date.OneDay -> {
                            dateItem(
                                dateContent = state.taskDateContent,
                                onClick = {}
                            )
                            itemDivider()
                        }

                        is UITaskContent.Date.Period -> {
                            dateItem(
                                dateContent = state.taskDateContent,
                                onStartDateClick = {},
                                onEndDateClick = {}
                            )
                            itemDivider()
                        }

                        else -> Unit
                    }
                }

                run {
                    timeAndRemindersItem(
                        reminderCount = state.taskRemindersCount,
                        onClick = {}
                    )
                    itemDivider()
                }

                run {
                    tagsItem(
                        tagCount = state.taskTagCount,
                        onClick = {}
                    )
                    itemDivider()
                }

                run {
                    priorityItem(
                        priority = state.taskPriority,
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Composable
private fun CreateTaskScreenConfigStateless(
    config: CreateTaskScreenConfig,
    onResultEvent: (CreateTaskScreenEvent.ResultEvent) -> Unit
) {
    when (config) {
        is CreateTaskScreenConfig.PickTitle -> {
            PickTaskTitleDialog(
                stateHolder = config.stateHolder,
                onResult = {
                    onResultEvent(CreateTaskScreenEvent.ResultEvent.PickTaskTitle(it))
                }
            )
        }

        is CreateTaskScreenConfig.PickTaskNumberProgress -> {
            PickTaskNumberProgressDialog(
                stateHolder = config.stateHolder,
                onResult = {
                    onResultEvent(CreateTaskScreenEvent.ResultEvent.PickTaskNumberProgress(it))
                }
            )
        }

        is CreateTaskScreenConfig.PickTaskTimeProgress -> {
            PickTaskTimeProgressDialog(
                stateHolder = config.stateHolder,
                onResult = {
                    onResultEvent(
                        CreateTaskScreenEvent.ResultEvent.PickTaskTimeProgress(it)
                    )
                }
            )
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

private fun LazyListScope.numberProgressItem(
    taskProgressContent: TaskContentModel.ProgressContent.Number,
    onClick: () -> Unit
) {
    item(
        key = ConfigItemType.DailyGoal,
        contentType = ConfigContentType.Standard
    ) {
        val dataText = remember(taskProgressContent) {
            taskProgressContent.toDisplay()
        }
        StandardItemConfig(
            iconResId = R.drawable.ic_goal,
            titleText = "Daily goal",
            dataText = dataText,
            onClick = onClick
        )
    }
}

private fun LazyListScope.timeProgressItem(
    taskProgressContent: TaskContentModel.ProgressContent.Time,
    onClick: () -> Unit
) {
    item(
        key = ConfigItemType.DailyGoal,
        contentType = ConfigContentType.Standard
    ) {
        val dataText = remember(taskProgressContent) {
            taskProgressContent.toDisplay()
        }
        StandardItemConfig(
            iconResId = R.drawable.ic_goal,
            titleText = "Daily goal",
            dataText = dataText,
            onClick = onClick
        )
    }
}

private fun LazyListScope.frequencyItem(
    frequencyContent: TaskContentModel.FrequencyContent.EveryDay,
    onClick: () -> Unit
) {
    item {
        val dataText = remember(frequencyContent) {
            frequencyContent.toDisplay()
        }
        StandardItemConfig(
            iconResId = R.drawable.ic_frequency,
            titleText = "Frequency",
            dataText = dataText,
            onClick = onClick
        )
    }
}

private fun LazyListScope.frequencyItem(
    frequencyContent: TaskContentModel.FrequencyContent.DaysOfWeek,
    onClick: () -> Unit
) {
    item {
        val dataText = remember(frequencyContent) {
            frequencyContent.toDisplay()
        }
        StandardItemConfig(
            iconResId = R.drawable.ic_frequency,
            titleText = "Frequency",
            dataText = dataText,
            onClick = onClick
        )
    }
}

private fun LazyListScope.dateItem(
    dateContent: UITaskContent.Date.OneDay,
    onClick: () -> Unit
) {
    item(
        key = ConfigItemType.Date,
        contentType = ConfigContentType.Standard
    ) {
        StandardItemConfig(
            iconResId = R.drawable.ic_start_date,
            titleText = "Date",
            dataText = dateContent.date.toDayMonthYear(),
            onClick = onClick
        )
    }
}

private fun LazyListScope.dateItem(
    dateContent: UITaskContent.Date.Period,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit
) {
    item(
        key = ConfigItemType.StartDate,
        contentType = ConfigContentType.Standard
    ) {
        StandardItemConfig(
            iconResId = R.drawable.ic_start_date,
            titleText = "Start date",
            dataText = dateContent.startDate.toDayMonthYear(),
            onClick = onStartDateClick
        )
    }
    itemDivider()
    item(
        key = ConfigItemType.EndDate,
        contentType = ConfigContentType.Switch
    ) {
        StandardItemConfig(
            iconResId = R.drawable.ic_end_date,
            titleText = "End date",
            dataText = dateContent.endDate?.toDayMonthYear() ?: " --- ",
            onClick = onEndDateClick
        )
    }
}

private fun LazyListScope.timeAndRemindersItem(
    reminderCount: Int,
    onClick: () -> Unit
) {
    item(
        key = ConfigItemType.TimeAndReminders,
        contentType = ConfigContentType.Standard
    ) {
        StandardItemConfig(
            iconResId = R.drawable.ic_notification,
            titleText = "Time and reminders",
            dataText = "$reminderCount",
            onClick = onClick
        )
    }
}

private fun LazyListScope.tagsItem(
    tagCount: Int,
    onClick: () -> Unit
) {
    item(
        key = ConfigItemType.Tags,
        contentType = ConfigContentType.Standard
    ) {
        StandardItemConfig(
            iconResId = R.drawable.ic_tag,
            titleText = "Tags",
            dataText = "$tagCount",
            onClick = onClick
        )
    }
}

private fun LazyListScope.priorityItem(
    priority: String,
    onClick: () -> Unit
) {
    item(
        key = ConfigItemType.Priority,
        contentType = ConfigContentType.Standard
    ) {
        StandardItemConfig(
            iconResId = R.drawable.ic_priority,
            titleText = "Priority",
            dataText = priority,
            onClick = onClick
        )
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
    EndDate,
    TimeAndReminders,
    Tags,
    Priority
}

private enum class ConfigContentType {
    Standard,
    Switch
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenTopBar(
    canSave: Boolean,
    onSaveClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Create activity")
        },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        },
        actions = {
            TextButton(
                onClick = onSaveClick,
                enabled = canSave
            ) {
                Text(text = "save")
            }
        }
    )
}