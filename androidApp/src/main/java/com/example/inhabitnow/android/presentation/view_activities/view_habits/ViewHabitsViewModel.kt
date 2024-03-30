package com.example.inhabitnow.android.presentation.view_activities.view_habits

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.view_activities.model.TaskFilterByStatus
import com.example.inhabitnow.android.presentation.view_activities.model.TaskSort
import com.example.inhabitnow.android.presentation.view_activities.view_habits.components.ViewHabitsScreenConfig
import com.example.inhabitnow.android.presentation.view_activities.view_habits.components.ViewHabitsScreenEvent
import com.example.inhabitnow.android.presentation.view_activities.view_habits.components.ViewHabitsScreenNavigation
import com.example.inhabitnow.android.presentation.view_activities.view_habits.components.ViewHabitsScreenState
import com.example.inhabitnow.domain.model.task.derived.FullTaskModel
import com.example.inhabitnow.domain.use_case.read_full_habits.ReadFullHabitsUseCase
import com.example.inhabitnow.domain.use_case.tag.read_tags.ReadTagsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ViewHabitsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    readFullHabitsUseCase: ReadFullHabitsUseCase,
    readTagsUseCase: ReadTagsUseCase,
    @DefaultDispatcherQualifier private val defaultDispatcher: CoroutineDispatcher
) : BaseViewModel<ViewHabitsScreenEvent, ViewHabitsScreenState, ViewHabitsScreenNavigation, ViewHabitsScreenConfig>() {
    private val filterByTagsIdsState = MutableStateFlow<Set<String>>(emptySet())
    private val filterByStatusState = MutableStateFlow<TaskFilterByStatus.HabitStatus?>(null)
    private val sortState = MutableStateFlow<TaskSort.HabitsSort?>(null)
    private val allHabitsFlow = readFullHabitsUseCase()
    private val allProcessedHabitsState = combine(
        allHabitsFlow,
        filterByTagsIdsState,
        filterByStatusState,
        sortState
    ) { allHabits, filterByTagsIds, filterByStatus, sort ->
        if (allHabits.isNotEmpty()) {
            withContext(defaultDispatcher) {
                allHabits
                    .asSequence()
                    .let { if (filterByTagsIds.isNotEmpty()) it.filterByTags(filterByTagsIds) else it }
                    .let { if (filterByStatus != null) it.filterByStatus(filterByStatus) else it }
                    .let { if (sort != null) it.sortHabits(sort) else it }
                    .toList()
            }
        } else emptyList()
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    private val allTagsState = readTagsUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    override val uiScreenState: StateFlow<ViewHabitsScreenState> =
        combine(
            allProcessedHabitsState,
            allTagsState,
            filterByTagsIdsState,
            filterByStatusState,
            sortState
        ) { allProcessedHabits, allTags, filterByTagsIds, filterByStatus, sort ->
            ViewHabitsScreenState(
                allHabits = allProcessedHabits,
                allTags = allTags,
                filterByTagsIds = filterByTagsIds,
                filterByStatus = filterByStatus,
                sort = sort
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ViewHabitsScreenState(
                allHabits = allProcessedHabitsState.value,
                allTags = allTagsState.value,
                filterByTagsIds = filterByTagsIdsState.value,
                filterByStatus = filterByStatusState.value,
                sort = sortState.value
            )
        )

    override fun onEvent(event: ViewHabitsScreenEvent) {

    }

    private fun Sequence<FullTaskModel.FullHabit>.filterByTags(
        filterByTagsIds: Set<String>
    ): Sequence<FullTaskModel.FullHabit> = this.let { allHabits ->
        if (filterByTagsIds.isEmpty()) allHabits
        else allHabits.filter { fullHabit ->
            fullHabit.allTags.any { it.id in filterByTagsIds }
        }
    }

    private fun Sequence<FullTaskModel.FullHabit>.filterByStatus(
        filterByStatus: TaskFilterByStatus.HabitStatus
    ): Sequence<FullTaskModel.FullHabit> = this.let { allHabits ->
        when (filterByStatus) {
            is TaskFilterByStatus.OnlyActive -> {
                allHabits.filter { fullHabit ->
                    !fullHabit.taskModel.isArchived
                }
            }

            is TaskFilterByStatus.OnlyArchived -> {
                allHabits.filter { fullHabit ->
                    fullHabit.taskModel.isArchived
                }
            }
        }
    }

    private fun Sequence<FullTaskModel.FullHabit>.sortHabits(
        sort: TaskSort.HabitsSort
    ): Sequence<FullTaskModel.FullHabit> = this.let { allHabits ->
        when (sort) {
            is TaskSort.ByStartDate -> {
                allHabits.sortedByDescending { it.taskModel.dateContent.startDate }
            }

            is TaskSort.ByPriority -> {
                allHabits.sortedByDescending { it.taskModel.priority }
            }

            is TaskSort.ByTitle -> {
                allHabits.sortedBy { it.taskModel.title }
            }
        }
    }

}