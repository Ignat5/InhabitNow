package com.example.inhabitnow.android.presentation.view_activities.view_habits.components

import androidx.compose.runtime.Stable
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.model.SelectableTagModel
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.android.presentation.view_activities.model.TaskFilterByStatus
import com.example.inhabitnow.android.presentation.view_activities.model.TaskSort
import com.example.inhabitnow.domain.model.tag.TagModel
import com.example.inhabitnow.domain.model.task.derived.FullTaskModel

@Stable
data class ViewHabitsScreenState(
    val allTasksResult: UIResultModel<List<FullTaskModel.FullHabit>>,
    val allSelectableTags: List<SelectableTagModel>,
    val filterByStatus: TaskFilterByStatus.HabitStatus?,
    val sort: TaskSort.HabitsSort?
) : ScreenState
