package com.example.inhabitnow.android.ui

import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.view_schedule.model.TaskScheduleStatusType
import com.example.inhabitnow.core.type.ProgressLimitType
import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.domain.model.reminder.content.ReminderContentModel
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month

fun TaskContentModel.ProgressContent.Time.toDisplay(): String {
    val prefix = "${this.limitType.toDisplay()} ${this.limitTime.toHourMinute()}"
    return "$prefix a day"
}

fun TaskContentModel.ProgressContent.Number.toDisplay(): String {
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

fun TaskContentModel.FrequencyContent.EveryDay.toDisplay() = "Every day"

fun TaskContentModel.FrequencyContent.DaysOfWeek.toDisplay(): String {
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

fun DayOfWeek.toDisplayShort(length: Int = 3) = this.toDisplay().take(length)

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

fun LocalDate.toMonthDay(): String {
    val month = this.month.toDisplay()
    val dayOfMonth = this.dayOfMonth
    return "$month $dayOfMonth"
}

fun LocalDate.toShortMonthDayYear(): String {
    val month = this.month.toDisplay().take(3)
    val dayOfMonth = this.dayOfMonth
    val year = this.year
    return "$month $dayOfMonth, $year"
}

private fun Int.insertZeroIfRequired(): String = this.let { number ->
    if (number <= 9) "0$number" else "$number"
}

fun ReminderType.toIconResId(): Int = when (this) {
    ReminderType.NoReminder -> R.drawable.ic_notification_off
    ReminderType.Notification -> R.drawable.ic_notification
}

fun TaskContentModel.DateContent.toDatePeriodDisplay(): String = this.let { dateContent ->
    when (dateContent) {
        is TaskContentModel.DateContent.Day -> dateContent.date.toDayMonthYear()
        is TaskContentModel.DateContent.Period -> dateContent.endDate?.let { endDate ->
            "${dateContent.startDate.toDayMonthYear()} - ${endDate.toDayMonthYear()}"
        } ?: "starting ${dateContent.startDate.toDayMonthYear()}"
    }
}

fun TaskType.toDisplay() = when (this) {
    TaskType.SingleTask -> "Task"
    TaskType.RecurringTask -> "Recurring task"
    TaskType.Habit -> "Habit"
}

private const val DEFAULT_REMINDER: Double = 0.0
private const val DEFAULT_DELIMITER: Int = 1

fun Double.limitNumberToString(): String = this.let { number ->
    if (number.rem(DEFAULT_DELIMITER) == DEFAULT_REMINDER) {
        "${number.toInt()}"
    } else "$number"
}

fun TaskScheduleStatusType.toDisplay() = when (this) {
    is TaskScheduleStatusType.Pending -> "pending"
    is TaskScheduleStatusType.InProgress -> "in progress"
    is TaskScheduleStatusType.Done -> "done"
    is TaskScheduleStatusType.Skipped -> "skipped"
    is TaskScheduleStatusType.Failed -> "failed"
}