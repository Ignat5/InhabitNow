package com.example.inhabitnow.domain.model.record

import com.example.inhabitnow.domain.model.record.content.RecordContentModel
import kotlinx.datetime.LocalDate

data class RecordModel(
    val id: String,
    val taskId: String,
    val date: LocalDate,
    val entry: RecordContentModel.Entry,
    val createdAt: Long
)
