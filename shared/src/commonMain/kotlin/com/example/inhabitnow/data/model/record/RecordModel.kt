package com.example.inhabitnow.data.model.record

import com.example.inhabitnow.data.model.record.content.RecordContentModel
import kotlinx.datetime.LocalDate

data class RecordModel(
    val id: String,
    val taskId: String,
    val date: LocalDate,
    val entry: RecordContentModel.Entry,
    val createdAt: Long
)
