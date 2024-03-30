package com.example.inhabitnow.data.model.task.derived

import com.example.inhabitnow.data.model.reminder.ReminderEntity
import com.example.inhabitnow.data.model.tag.TagEntity
import com.example.inhabitnow.data.model.task.TaskWithContentEntity

data class FullTaskEntity(
    val taskWithContentEntity: TaskWithContentEntity,
    val allReminders: List<ReminderEntity>,
    val allTags: List<TagEntity>
)
