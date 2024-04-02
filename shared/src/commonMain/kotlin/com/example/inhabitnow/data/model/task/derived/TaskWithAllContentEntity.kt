package com.example.inhabitnow.data.model.task.derived

import com.example.inhabitnow.data.model.task.TaskEntity
import com.example.inhabitnow.data.model.task.content.ArchiveContentEntity
import com.example.inhabitnow.data.model.task.content.FrequencyContentEntity
import com.example.inhabitnow.data.model.task.content.ProgressContentEntity

data class TaskWithAllContentEntity(
    val taskEntity: TaskEntity,
    val allProgressContent: List<ProgressContentEntity>,
    val allFrequencyContent: List<FrequencyContentEntity>,
    val allArchiveContent: List<ArchiveContentEntity>
)
