package com.example.inhabitnow.data.model.record

import com.example.inhabitnow.data.model.record.content.RecordContentEntity
import kotlinx.datetime.LocalDate

data class RecordEntity(
    val id: String,
    val taskId: String,
    val date: LocalDate,
    val entry: RecordContentEntity.Entry,
    val createdAt: Long
)
