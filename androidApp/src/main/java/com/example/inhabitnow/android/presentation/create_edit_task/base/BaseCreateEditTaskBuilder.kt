package com.example.inhabitnow.android.presentation.create_edit_task.base

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.common.pick_date.PickDateDialog
import com.example.inhabitnow.android.presentation.common.pick_date.components.PickDateScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenConfig
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.confirm_leave.ConfirmLeaveDialog
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.model.BaseItemTaskConfig
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description.PickTaskDescriptionDialog
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.PickTaskFrequencyDialog
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_priority.PickTaskPriorityDialog
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number.PickTaskNumberProgressDialog
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.time.PickTaskTimeProgressDialog
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.PickTaskTagsDialog
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.PickTaskTitleDialog
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenConfig
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenEvent
import com.example.inhabitnow.android.presentation.model.UITaskContent
import com.example.inhabitnow.android.ui.toDayMonthYear
import com.example.inhabitnow.android.ui.toDisplay

object BaseCreateEditTaskBuilder {

    @Composable
    fun BaseScreenConfig(
        baseConfig: BaseCreateEditTaskScreenConfig,
        onBaseResultEvent: (BaseCreateEditTaskScreenEvent.ResultEvent) -> Unit
    ) {
        when (baseConfig) {
            is BaseCreateEditTaskScreenConfig.PickTaskTitle -> {
                PickTaskTitleDialog(
                    stateHolder = baseConfig.stateHolder,
                    onResult = {
                        onBaseResultEvent(BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskTitle(it))
                    }
                )
            }
        }
    }

    fun baseConfigItems(
        lazyListScope: LazyListScope,
        allTaskConfigItems: List<BaseItemTaskConfig>,
        onItemClick: (BaseItemTaskConfig) -> Unit
    ) {
        lazyListScope.itemsIndexed(
            items = allTaskConfigItems,
            key = { _, item -> item.key },
            contentType = { _, item -> item.contentType }
        ) { index, item ->
            Column(modifier = Modifier.fillMaxWidth()) {
                val onClick = remember {
                    val callback: () -> Unit = { onItemClick(item) }
                    callback
                }
                when (item) {
                    is BaseItemTaskConfig.Title -> {
                        ItemTitleConfig(
                            item = item,
                            onClick = onClick
                        )
                    }

                    is BaseItemTaskConfig.Description -> {
                        ItemDescriptionConfig(
                            item = item,
                            onClick = onClick
                        )
                    }

                    is BaseItemTaskConfig.Progress -> {
                        ItemProgressConfig(
                            item = item,
                            onClick = onClick
                        )
                    }

                    is BaseItemTaskConfig.Frequency -> {
                        ItemFrequencyConfig(
                            item = item,
                            onClick = onClick
                        )
                    }

                    is BaseItemTaskConfig.Date -> {
                        when (item) {
                            is BaseItemTaskConfig.Date.OneDayDate -> {
                                ItemOneDayDateConfig(
                                    item = item,
                                    onClick = onClick
                                )
                            }

                            is BaseItemTaskConfig.Date.StartDate -> {
                                ItemStartDateConfig(
                                    item = item,
                                    onClick = onClick
                                )
                            }

                            is BaseItemTaskConfig.Date.EndDate -> {
                                ItemEndDateConfig(
                                    item = item,
                                    onClick = onClick,
                                    onSwitchClick = {
//                                        onEvent(CreateTaskScreenEvent.OnEndDateSwitchClick)
                                    }
                                )
                            }
                        }
                    }

                    is BaseItemTaskConfig.Reminders -> {
                        ItemRemindersConfig(
                            item = item,
                            onClick = onClick
                        )
                    }

                    is BaseItemTaskConfig.Tags -> {
                        ItemTagsConfig(
                            item = item,
                            onClick = onClick
                        )
                    }

                    is BaseItemTaskConfig.Priority -> {
                        ItemPriorityConfig(
                            item = item,
                            onClick = onClick
                        )
                    }
                }
                if (index != allTaskConfigItems.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }

    @Composable
    private fun ItemTitleConfig(
        item: BaseItemTaskConfig.Title,
        onClick: () -> Unit
    ) {
        BasicItemConfig(
            iconResId = R.drawable.ic_edit,
            titleText = "Activity name",
            dataText = item.title,
            onClick = onClick
        )
    }

    @Composable
    private fun ItemDescriptionConfig(
        item: BaseItemTaskConfig.Description,
        onClick: () -> Unit
    ) {
        BasicItemConfig(
            iconResId = R.drawable.ic_description,
            titleText = "Description",
            dataText = item.description,
            onClick = onClick
        )
    }

    @Composable
    private fun ItemProgressConfig(
        item: BaseItemTaskConfig.Progress,
        onClick: () -> Unit
    ) {
        val dataText = remember(item) {
            when (item) {
                is BaseItemTaskConfig.Progress.Number -> item.uiProgressContent.toDisplay()
                is BaseItemTaskConfig.Progress.Time -> item.uiProgressContent.toDisplay()
            }
        }
        BasicItemConfig(
            iconResId = R.drawable.ic_goal,
            titleText = "Daily goal",
            dataText = dataText,
            onClick = onClick
        )
    }

    @Composable
    private fun ItemFrequencyConfig(
        item: BaseItemTaskConfig.Frequency,
        onClick: () -> Unit
    ) {
        val dataText = remember(item) {
            when (val fc = item.uiFrequencyContent) {
                is UITaskContent.Frequency.EveryDay -> fc.toDisplay()
                is UITaskContent.Frequency.DaysOfWeek -> fc.toDisplay()
            }
        }
        BasicItemConfig(
            iconResId = R.drawable.ic_frequency,
            titleText = "Frequency",
            dataText = dataText,
            onClick = onClick
        )
    }

    @Composable
    private fun ItemOneDayDateConfig(
        item: BaseItemTaskConfig.Date.OneDayDate,
        onClick: () -> Unit
    ) {
        val dataText = remember(item) {
            item.date.toDayMonthYear()
        }
        BasicItemConfig(
            iconResId = R.drawable.ic_start_date,
            titleText = "Date",
            dataText = dataText,
            onClick = onClick
        )
    }

    @Composable
    private fun ItemStartDateConfig(
        item: BaseItemTaskConfig.Date.StartDate,
        onClick: () -> Unit
    ) {
        val dataText = remember(item) {
            item.date.toDayMonthYear()
        }
        BasicItemConfig(
            iconResId = R.drawable.ic_start_date,
            titleText = "Start date",
            dataText = dataText,
            onClick = onClick
        )
    }

    @Composable
    private fun ItemEndDateConfig(
        item: BaseItemTaskConfig.Date.EndDate,
        onClick: () -> Unit,
        onSwitchClick: () -> Unit
    ) {
        val dataText = remember(item) {
            item.date?.toDayMonthYear() ?: ""
        }
        val isChecked = remember(item) {
            item.date != null
        }
        BaseItemConfigWithSwitch(
            iconResId = R.drawable.ic_end_date,
            titleText = "End date",
            dataText = dataText,
            isChecked = isChecked,
            onClick = onClick,
            onSwitchClick = { onSwitchClick() }
        )
    }

    @Composable
    private fun ItemRemindersConfig(
        item: BaseItemTaskConfig.Reminders,
        onClick: () -> Unit
    ) {
        BasicItemConfig(
            iconResId = R.drawable.ic_notification,
            titleText = "Time and reminders",
            dataText = "${item.count}",
            onClick = onClick
        )
    }

    @Composable
    private fun ItemTagsConfig(
        item: BaseItemTaskConfig.Tags,
        onClick: () -> Unit
    ) {
        BasicItemConfig(
            iconResId = R.drawable.ic_tag,
            titleText = "Tags",
            dataText = "${item.count}",
            onClick = onClick
        )
    }

    @Composable
    private fun ItemPriorityConfig(
        item: BaseItemTaskConfig.Priority,
        onClick: () -> Unit
    ) {
        BasicItemConfig(
            iconResId = R.drawable.ic_priority,
            titleText = "Priority",
            dataText = item.priority,
            onClick = onClick
        )
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

            is CreateTaskScreenConfig.PickTaskFrequency -> {
                PickTaskFrequencyDialog(
                    stateHolder = config.stateHolder,
                    onResult = {
                        onResultEvent(CreateTaskScreenEvent.ResultEvent.PickTaskFrequency(it))
                    }
                )
            }

            is CreateTaskScreenConfig.PickTaskTags -> {
                PickTaskTagsDialog(
                    stateHolder = config.stateHolder,
                    onResult = {
                        onResultEvent(CreateTaskScreenEvent.ResultEvent.PickTaskTags(it))
                    }
                )
            }

            is CreateTaskScreenConfig.PickDate -> {
                val onResult: (PickDateScreenResult) -> Unit = remember {
                    val callback: (PickDateScreenResult) -> Unit = {
                        when (config) {
                            is CreateTaskScreenConfig.PickDate.StartDate -> {
                                onResultEvent(
                                    CreateTaskScreenEvent.ResultEvent.PickDate.StartDate(
                                        it
                                    )
                                )
                            }

                            is CreateTaskScreenConfig.PickDate.EndDate -> {
                                onResultEvent(CreateTaskScreenEvent.ResultEvent.PickDate.EndDate(it))
                            }

                            is CreateTaskScreenConfig.PickDate.OneDayDate -> {
                                onResultEvent(
                                    CreateTaskScreenEvent.ResultEvent.PickDate.OneDayDate(
                                        it
                                    )
                                )
                            }
                        }
                    }
                    callback
                }
                PickDateDialog(
                    stateHolder = config.stateHolder,
                    onResult = onResult
                )
            }

            is CreateTaskScreenConfig.PickTaskDescription -> {
                PickTaskDescriptionDialog(
                    stateHolder = config.stateHolder,
                    onResult = {
                        onResultEvent(CreateTaskScreenEvent.ResultEvent.PickTaskDescription(it))
                    }
                )
            }

            is CreateTaskScreenConfig.PickTaskPriority -> {
                PickTaskPriorityDialog(
                    stateHolder = config.stateHolder,
                    onResult = {
                        onResultEvent(CreateTaskScreenEvent.ResultEvent.PickTaskPriority(it))
                    }
                )
            }

            is CreateTaskScreenConfig.ConfirmLeave -> {
                ConfirmLeaveDialog(
                    onResult = {
                        onResultEvent(CreateTaskScreenEvent.ResultEvent.ConfirmLeave(it))
                    }
                )
            }
        }
    }

    @Composable
    private fun BasicItemConfig(
        @DrawableRes iconResId: Int,
        titleText: String,
        dataText: String,
        onClick: () -> Unit
    ) {
        BaseItemConfigContainer(
            iconResId = iconResId,
            titleText = titleText,
            dataContent = {
                Text(
                    text = dataText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            onClick = onClick
        )
    }

    @Composable
    private fun NumberItemConfig(
        @DrawableRes iconResId: Int,
        titleText: String,
        dataText: String,
        onClick: () -> Unit
    ) {
        BaseItemConfigContainer(
            iconResId = iconResId,
            titleText = titleText,
            dataContent = {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dataText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
                    )
                }
            },
            onClick = onClick
        )
    }

    @Composable
    private fun BaseItemConfigWithSwitch(
        @DrawableRes iconResId: Int,
        titleText: String,
        dataText: String,
        isChecked: Boolean,
        onClick: () -> Unit,
        onSwitchClick: (Boolean) -> Unit
    ) {
        BaseItemConfigContainer(
            iconResId = iconResId,
            titleText = titleText,
            onClick = onClick,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            dataContent = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dataText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = isChecked,
                        onCheckedChange = onSwitchClick
                    )
                }
            }
        )
    }

    @Composable
    private fun BaseItemConfigContainer(
        @DrawableRes iconResId: Int,
        titleText: String,
        dataContent: @Composable BoxScope.() -> Unit,
        onClick: () -> Unit,
        contentPadding: PaddingValues = PaddingValues(16.dp)
    ) {
        Box(
            modifier = Modifier
                .clickable { onClick() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
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
                )
                Box(modifier = Modifier.weight(1f)) {
                    dataContent()
                }
            }
        }
    }
}