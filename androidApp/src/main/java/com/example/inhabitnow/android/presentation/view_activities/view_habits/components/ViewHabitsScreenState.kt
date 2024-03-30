package com.example.inhabitnow.android.presentation.view_activities.view_habits.components

import androidx.compose.runtime.Stable
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.view_activities.model.TaskFilterByStatus
import com.example.inhabitnow.android.presentation.view_activities.model.TaskSort
import com.example.inhabitnow.domain.model.tag.TagModel
import com.example.inhabitnow.domain.model.task.derived.FullTaskModel

@Stable
data class ViewHabitsScreenState(
    val allHabits: List<FullTaskModel.FullHabit>,
    val allTags: List<TagModel>,
    val filterByTagsIds: Set<String>,
    val filterByStatus: TaskFilterByStatus.HabitStatus?,
    val sort: TaskSort.HabitsSort?
) : ScreenState
