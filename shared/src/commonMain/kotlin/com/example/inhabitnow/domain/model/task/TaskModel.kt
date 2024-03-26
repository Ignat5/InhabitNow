package com.example.inhabitnow.domain.model.task

import com.example.inhabitnow.core.type.TaskProgressType
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import kotlinx.datetime.LocalDate

sealed class TaskModel {
    abstract val id: String
    abstract val type: TaskType
    abstract val progressType: TaskProgressType
    abstract val title: String
    abstract val description: String
    abstract val priority: Int
    abstract val dateContent: TaskContentModel.DateContent
    abstract val isArchived: Boolean
    abstract val createdAt: Long

    sealed interface RecurringActivity {
        val frequencyContent: TaskContentModel.FrequencyContent
    }

    sealed class Habit(
        final override val progressType: TaskProgressType
    ) : TaskModel(), RecurringActivity {
        final override val type: TaskType = TaskType.Habit
        abstract override val dateContent: TaskContentModel.DateContent.Period

        sealed class HabitContinuous(progressType: TaskProgressType) : Habit(progressType) {
            abstract val progressContent: TaskContentModel.ProgressContent

            data class HabitNumber(
                override val id: String,
                override val title: String,
                override val description: String,
                override val priority: Int,
                override val isArchived: Boolean,
                override val progressContent: TaskContentModel.ProgressContent.Number,
                override val frequencyContent: TaskContentModel.FrequencyContent,
                override val dateContent: TaskContentModel.DateContent.Period,
                override val createdAt: Long
            ) : HabitContinuous(TaskProgressType.Number)

            data class HabitTime(
                override val id: String,
                override val title: String,
                override val description: String,
                override val priority: Int,
                override val isArchived: Boolean,
                override val progressContent: TaskContentModel.ProgressContent.Time,
                override val frequencyContent: TaskContentModel.FrequencyContent,
                override val dateContent: TaskContentModel.DateContent.Period,
                override val createdAt: Long
            ) : HabitContinuous(TaskProgressType.Time)

        }

        data class HabitYesNo(
            override val id: String,
            override val title: String,
            override val description: String,
            override val priority: Int,
            override val isArchived: Boolean,
            override val frequencyContent: TaskContentModel.FrequencyContent,
            override val dateContent: TaskContentModel.DateContent.Period,
            override val createdAt: Long
        ) : Habit(TaskProgressType.YesNo)
    }

    sealed class Task(final override val type: TaskType) : TaskModel() {
        final override val progressType: TaskProgressType = TaskProgressType.YesNo

        data class SingleTask(
            override val id: String,
            override val title: String,
            override val description: String,
            override val priority: Int,
            override val isArchived: Boolean,
            override val dateContent: TaskContentModel.DateContent.Day,
            override val createdAt: Long
        ) : Task(TaskType.SingleTask)

        data class RecurringTask(
            override val id: String,
            override val title: String,
            override val description: String,
            override val priority: Int,
            override val isArchived: Boolean,
            override val frequencyContent: TaskContentModel.FrequencyContent,
            override val dateContent: TaskContentModel.DateContent.Period,
            override val createdAt: Long
        ) : Task(TaskType.RecurringTask), RecurringActivity
    }
}

//data class TaskModel(
//    val id: String,
//    val type: TaskType,
//    val progressType: TaskProgressType,
//    val title: String,
//    val description: String,
//    val startDate: LocalDate,
//    val endDate: LocalDate?,
//    val priority: String,
//    val createdAt: Long,
//    val deletedAt: Long?
//)
