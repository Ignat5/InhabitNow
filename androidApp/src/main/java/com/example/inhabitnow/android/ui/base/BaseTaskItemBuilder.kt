package com.example.inhabitnow.android.ui.base

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.ui.toDayMonthYear
import com.example.inhabitnow.android.ui.toDisplay
import com.example.inhabitnow.android.ui.toHourMinute
import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.core.type.TaskProgressType
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.domain.model.reminder.ReminderModel
import com.example.inhabitnow.domain.model.tag.TagModel
import kotlinx.datetime.LocalDate

object BaseTaskItemBuilder {

    @Composable
    fun TaskDivider() {
        HorizontalDivider(modifier = Modifier.alpha(0.2f))
    }

    @Composable
    fun ChipTaskReminders(allReminders: List<ReminderModel>) {
        if (allReminders.isEmpty()) return
        val aStr = remember(allReminders) {
            buildAnnotatedString {
                allReminders.forEachIndexed { index, reminder ->
                    append(reminder.time.toHourMinute())
                    if (index != allReminders.lastIndex) {
                        append(" • ")
                    }
                }
            }
        }
        BaseIconDataItem(
            iconResId = R.drawable.ic_clock,
            text = aStr.text
        )
    }

    @Composable
    private fun ItemReminder(reminder: ReminderModel) {
        val iconResId = remember(reminder) {
            when (reminder.type) {
                ReminderType.NoReminder -> R.drawable.ic_notification_off
                ReminderType.Notification -> R.drawable.ic_notification
            }
        }
        val text = remember(reminder) {
            reminder.time.toHourMinute()
        }
        BaseIconDataItem(
            iconResId = iconResId,
            text = text
        )
    }

    @Composable
    fun ChipTaskTags(allTags: List<TagModel>) {
        if (allTags.isEmpty()) return
        val aStr = remember(allTags) {
            buildAnnotatedString {
                allTags.forEachIndexed { index, tagModel ->
                    append(tagModel.title)
                    if (index != allTags.lastIndex) {
                        append(" • ")
                    }
                }
            }
        }
        BaseIconDataItem(
            iconResId = R.drawable.ic_tag,
            text = aStr.text
        )

//        val aStr = remember(allTags) {
//            buildAnnotatedString {
//                allTags.forEachIndexed { index, tagModel ->
//                    append(tagModel.title)
//                    if (index != allTags.lastIndex) {
//                        append(" | ")
//                    }
//                }
//            }
//        }
//        BaseIconDataItem(
//            iconResId = R.drawable.ic_tag,
//            text = aStr.text
//        )
    }

    @Composable
    fun ChipTaskStartDate(date: LocalDate) {
        BaseIconDataItem(
            iconResId = R.drawable.ic_start_date,
            text = date.toDayMonthYear()
        )
    }

    @Composable
    fun ChipTaskEndDate(date: LocalDate) {
        BaseIconDataItem(
            iconResId = R.drawable.ic_end_date,
            text = date.toDayMonthYear()
        )
    }

    @Composable
    fun ChipTaskPriority(priority: Int) {
        BaseIconDataItem(
            iconResId = R.drawable.ic_priority,
            text = "$priority"
        )
    }

    @Composable
    fun ChipTaskType(taskType: TaskType) {
        BaseIconItem(
            iconResId = when (taskType) {
                TaskType.Habit -> R.drawable.ic_habit
                TaskType.RecurringTask -> R.drawable.ic_recurring_task
                TaskType.SingleTask -> R.drawable.ic_task
            },
        )
    }

    @Composable
    fun ChipTaskProgressType(taskProgressType: TaskProgressType) {
        BaseIconItem(
            iconResId = when (taskProgressType) {
                TaskProgressType.YesNo -> R.drawable.ic_progress_yes_no
                TaskProgressType.Number -> R.drawable.ic_progress_number
                TaskProgressType.Time -> R.drawable.ic_progress_time
            },
        )
    }

    @Composable
    private fun BaseIconDataItem(
        @DrawableRes iconResId: Int,
        text: String
    ) {
        ItemDetailContainer {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    @Composable
    private fun BaseIconItem(
        @DrawableRes iconResId: Int
    ) {
        ItemDetailContainer {
            Icon(
                painter = painterResource(iconResId),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }

    @Composable
    private fun BaseDataItem(
        text: String
    ) {
        ItemDetailContainer {
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
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
}