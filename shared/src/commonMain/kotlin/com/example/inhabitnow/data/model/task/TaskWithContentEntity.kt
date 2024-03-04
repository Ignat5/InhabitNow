package com.example.inhabitnow.data.model.task

import com.example.inhabitnow.data.model.task.content.ArchiveContentEntity
import com.example.inhabitnow.data.model.task.content.FrequencyContentEntity
import com.example.inhabitnow.data.model.task.content.ProgressContentEntity

data class TaskWithContentEntity(
    val task: TaskEntity,
    val progressContent: ProgressContentEntity,
    val frequencyContent: FrequencyContentEntity,
    val archiveContent: ArchiveContentEntity
)
