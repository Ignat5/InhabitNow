package com.example.inhabitnow.android.ui

import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.model.UITaskContent
import com.example.inhabitnow.core.type.ProgressLimitType
import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.domain.model.reminder.content.ReminderContentModel
import com.example.inhabitnow.domain.model.task.TaskModel
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month

fun UITaskContent.Progress.Time.toDisplay(): String {
    val prefix = "${this.limitType.toDisplay()} ${this.limitTime.toHourMinute()}"
    return "$prefix a day"
}

fun UITaskContent.Progress.Number.toDisplay(): String {
    val prefix = "${this.limitType.toDisplay()} ${this.limitNumber}".let {
        if (this.limitUnit.isNotBlank()) "$it ${this.limitUnit}"
        else it
    }
    return "$prefix a day"
}

fun ProgressLimitType.toDisplay() = when (this) {
    ProgressLimitType.AtLeast -> "At least"
    ProgressLimitType.Exactly -> "Exactly"
//    ProgressLimitType.NoMoreThan -> "No more than"
}

fun UITaskContent.Frequency.EveryDay.toDisplay() = "Every day"

fun UITaskContent.Frequency.DaysOfWeek.toDisplay(): String {
    return this.daysOfWeek.toDisplay()
}

fun ReminderContentModel.ScheduleContent.EveryDay.toDisplay(): String = "Always enabled"

fun ReminderContentModel.ScheduleContent.DaysOfWeek.toDisplay(): String {
    return this.daysOfWeek.toDisplay()
}

fun ReminderContentModel.ScheduleContent.toDisplay() = when (this) {
    is ReminderContentModel.ScheduleContent.EveryDay -> this.toDisplay()
    is ReminderContentModel.ScheduleContent.DaysOfWeek -> this.toDisplay()
}

fun ReminderType.toDisplay() = when (this) {
    ReminderType.NoReminder -> "No reminder"
    ReminderType.Notification -> "Notification"
}

fun Collection<DayOfWeek>.toDisplay(): String {
    var result: String = ""
    val daysOfWeek = this.toList().sortedBy { it.ordinal }
    daysOfWeek.forEachIndexed { index, dayOfWeek ->
        result += dayOfWeek.toDisplay().take(3)
        if (index != daysOfWeek.lastIndex) result += " | "
    }
    return result
}

fun DayOfWeek.toDisplay() = when (this) {
    DayOfWeek.MONDAY -> "Monday"
    DayOfWeek.TUESDAY -> "Tuesday"
    DayOfWeek.WEDNESDAY -> "Wednesday"
    DayOfWeek.THURSDAY -> "Thursday"
    DayOfWeek.FRIDAY -> "Friday"
    DayOfWeek.SATURDAY -> "Saturday"
    DayOfWeek.SUNDAY -> "Sunday"
}

fun Month.toDisplay() = when (this) {
    Month.JANUARY -> "January"
    Month.FEBRUARY -> "February"
    Month.MARCH -> "March"
    Month.APRIL -> "April"
    Month.MAY -> "May"
    Month.JUNE -> "June"
    Month.JULY -> "July"
    Month.AUGUST -> "August"
    Month.SEPTEMBER -> "September"
    Month.OCTOBER -> "October"
    Month.NOVEMBER -> "November"
    Month.DECEMBER -> "December"
}

fun LocalTime.toHourMinute(): String {
    val hour = this.hour.insertZeroIfRequired()
    val minute = this.minute.insertZeroIfRequired()
    return "$hour:$minute"
}

fun LocalDate.toDayMonthYear(): String {
    val day = this.dayOfMonth.insertZeroIfRequired()
    val month = this.monthNumber.insertZeroIfRequired()
    val year = this.year
    return "$day.$month.$year"
}

fun LocalDate.toDayOfWeekMonthDayOfMonth(): String {
    val dayOfWeek = this.dayOfWeek.toDisplay()
    val month = this.month.toDisplay()
    val dayOfMonth = this.dayOfMonth
    return "$dayOfWeek, $month $dayOfMonth"
}

fun LocalDate.toMonthYear(): String {
    val month = this.month.toDisplay()
    val year = this.year
    return "$month $year"
}

private fun Int.insertZeroIfRequired(): String = this.let { number ->
    if (number <= 9) "0$number" else "$number"
}

fun ReminderType.toIconResId(): Int = when (this) {
    ReminderType.NoReminder -> R.drawable.ic_notification_off
    ReminderType.Notification -> R.drawable.ic_notification
}

fun TaskModel.toDatePeriodDisplay(): String = this.let { task ->
    when (task.type) {
        TaskType.SingleTask -> task.startDate.toDayMonthYear()
        TaskType.RecurringTask, TaskType.Habit -> {
            task.endDate?.let { endDate ->
                "${task.startDate.toDayMonthYear()} - ${endDate.toDayMonthYear()}"
            } ?: "starting ${task.startDate.toDayMonthYear()}"
        }
    }
}

fun TaskType.toDisplay() = when (this) {
    TaskType.SingleTask -> "Task"
    TaskType.RecurringTask -> "Recurring task"
    TaskType.Habit -> "Habit"
}