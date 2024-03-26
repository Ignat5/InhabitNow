package com.example.inhabitnow.domain.model.record.content

import kotlinx.datetime.LocalTime

sealed interface RecordContentModel {
    sealed interface Entry : RecordContentModel {
        data object Done : HabitEntry.HabitYesNoEntry, TaskEntry
        data object Skip : HabitEntry.HabitYesNoEntry, HabitEntry.HabitContinuousEntry.Number, HabitEntry.HabitContinuousEntry.Time
        data object Fail : HabitEntry.HabitYesNoEntry, HabitEntry.HabitContinuousEntry.Number, HabitEntry.HabitContinuousEntry.Time

        data class Number(val number: Double) : HabitEntry.HabitContinuousEntry.Number
        data class Time(val time: LocalTime) : HabitEntry.HabitContinuousEntry.Time

        sealed interface HabitEntry : Entry {
            sealed interface HabitContinuousEntry : HabitEntry {
                sealed interface Number : HabitContinuousEntry
                sealed interface Time : HabitContinuousEntry
            }
            sealed interface HabitYesNoEntry : HabitEntry
        }

        sealed interface TaskEntry : Entry
    }
}