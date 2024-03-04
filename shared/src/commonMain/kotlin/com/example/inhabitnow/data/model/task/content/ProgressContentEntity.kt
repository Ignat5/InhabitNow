package com.example.inhabitnow.data.model.task.content

import com.example.inhabitnow.data.model.task.content.base.TaskContentEntity
import com.example.inhabitnow.data.model.task.content.base.BaseTaskContentEntity

data class ProgressContentEntity(
    override val id: String,
    override val taskId: String,
    override val content: TaskContentEntity.ProgressContent,
    override val startEpochDay: Long,
    override val createdAt: Long
) : BaseTaskContentEntity<TaskContentEntity.ProgressContent>
