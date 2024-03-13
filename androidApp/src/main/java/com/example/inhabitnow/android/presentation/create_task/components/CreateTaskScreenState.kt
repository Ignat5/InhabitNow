package com.example.inhabitnow.android.presentation.create_task.components

import androidx.compose.runtime.Immutable
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.create_task.UITaskContent
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.domain.model.task.TaskWithContentModel
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import kotlinx.datetime.LocalDate

@Immutable
data class CreateTaskScreenState(
    val taskTitle: String,
    val taskDescription: String,
    val taskPriority: String,
    val taskProgressContent: UITaskContent.Progress?,
    val taskFrequencyContent: UITaskContent.Frequency?,
    val taskDateContent: UITaskContent.Date?,
    val taskRemindersCount: Int,
    val taskTagCount: Int,
    val canSave: Boolean
) : ScreenState
