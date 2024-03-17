package com.example.inhabitnow.data.model.task.content

import kotlinx.datetime.LocalDate

sealed interface BaseTaskContentEntity {
    val id: String
    val taskId: String
    val content: TaskContentEntity
    val startDate: LocalDate
    val createdAt: Long
}

data class ProgressContentEntity(
    override val id: String,
    override val taskId: String,
    override val content: TaskContentEntity.ProgressContent,
    override val startDate: LocalDate,
    override val createdAt: Long
) : BaseTaskContentEntity

data class FrequencyContentEntity(
    override val id: String,
    override val taskId: String,
    override val content: TaskContentEntity.FrequencyContent,
    override val startDate: LocalDate,
    override val createdAt: Long
) : BaseTaskContentEntity

data class ArchiveContentEntity(
    override val id: String,
    override val taskId: String,
    override val content: TaskContentEntity.ArchiveContent,
    override val startDate: LocalDate,
    override val createdAt: Long
) : BaseTaskContentEntity