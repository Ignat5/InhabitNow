package com.example.inhabitnow.android.presentation.create_edit_task.create.components

import androidx.compose.runtime.Immutable
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.model.UITaskContent

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
