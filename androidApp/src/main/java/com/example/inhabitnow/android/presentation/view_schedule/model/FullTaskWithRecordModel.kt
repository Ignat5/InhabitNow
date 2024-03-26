package com.example.inhabitnow.android.presentation.view_schedule.model

import com.example.inhabitnow.domain.model.reminder.ReminderModel
import com.example.inhabitnow.domain.model.tag.TagModel

data class FullTaskWithRecordModel(
    val taskWithRecordModel: TaskWithRecordModel,
    val allReminders: List<ReminderModel>,
    val allTags: List<TagModel>
)