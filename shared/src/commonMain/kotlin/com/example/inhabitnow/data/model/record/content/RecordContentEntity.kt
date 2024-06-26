package com.example.inhabitnow.data.model.record.content

import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("RecordContent")
@Serializable
sealed interface RecordContentEntity {
    @Serializable
    @SerialName("RecordContent.Entry")
    sealed interface Entry : RecordContentEntity {
        @SerialName("RecordContent.Entry.Done")
        @Serializable
        data object Done : Entry

        @SerialName("RecordContent.Entry.Skip")
        @Serializable
        data object Skip : Entry

        @SerialName("RecordContent.Entry.Fail")
        @Serializable
        data object Fail : Entry

        @SerialName("RecordContent.Entry.Number")
        @Serializable
        data class Number(val number: Double) : Entry

        @SerialName("RecordContent.Entry.Time")
        @Serializable
        data class Time(val time: LocalTime) : Entry
    }
}