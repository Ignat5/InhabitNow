package com.example.inhabitnow.domain.model.record.content

import kotlinx.datetime.LocalTime

sealed interface RecordContentModel {
    sealed interface Entry : RecordContentModel {
        data object Done : Entry
        data object Skip : Entry
        data object Fail : Entry
        data class Number(val number: String) : Entry
        data class Time(val time: LocalTime) : Entry
    }
}