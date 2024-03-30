package com.example.inhabitnow.domain.model.task.derived

import com.example.inhabitnow.domain.model.reminder.ReminderModel
import com.example.inhabitnow.domain.model.tag.TagModel
import com.example.inhabitnow.domain.model.task.TaskModel

//data class FullTaskModel(
//    val taskModel: TaskModel,
//    val allReminders: List<ReminderModel>,
//    val allTags: List<TagModel>
//)

sealed interface FullTaskModel {
    val taskModel: TaskModel
    val allReminders: List<ReminderModel>
    val allTags: List<TagModel>

    data class FullHabit(
        override val taskModel: TaskModel.Habit,
        override val allReminders: List<ReminderModel>,
        override val allTags: List<TagModel>
    ) : FullTaskModel

    data class FullTask(
        override val taskModel: TaskModel.Task,
        override val allReminders: List<ReminderModel>,
        override val allTags: List<TagModel>
    ) : FullTaskModel
}
