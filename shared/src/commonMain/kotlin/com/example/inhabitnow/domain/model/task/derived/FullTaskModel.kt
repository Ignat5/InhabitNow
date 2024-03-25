package com.example.inhabitnow.domain.model.task.derived

import com.example.inhabitnow.domain.model.reminder.ReminderModel
import com.example.inhabitnow.domain.model.tag.TagModel
import com.example.inhabitnow.domain.model.task.TaskWithContentModel

data class FullTaskModel(
    val taskWithContentModel: TaskWithContentModel,
    val allReminders: List<ReminderModel>,
    val allTags: List<TagModel>
)
