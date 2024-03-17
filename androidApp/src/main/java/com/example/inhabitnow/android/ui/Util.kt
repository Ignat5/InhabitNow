package com.example.inhabitnow.android.ui

import com.example.inhabitnow.android.presentation.model.UITaskContent
import com.example.inhabitnow.core.type.ProgressLimitType
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

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
    ProgressLimitType.NoMoreThan -> "No more than"
}

fun UITaskContent.Frequency.EveryDay.toDisplay() = "Every day"

fun UITaskContent.Frequency.DaysOfWeek.toDisplay(): String {
    var result: String = ""
    val daysOfWeek = this.daysOfWeek.toList().sortedBy { it.ordinal }
    daysOfWeek.forEachIndexed { index, dayOfWeek ->
        result += dayOfWeek.toDisplay().take(3)
        if (index != daysOfWeek.lastIndex) result += ", "
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

private fun Int.insertZeroIfRequired(): String = this.let { number ->
    if (number <= 9) "0$number" else "$number"
}