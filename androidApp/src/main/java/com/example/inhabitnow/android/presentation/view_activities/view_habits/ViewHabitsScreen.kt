package com.example.inhabitnow.android.presentation.view_activities.view_habits

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.model.SelectableTagModel
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_archive.ConfirmArchiveTaskDialog
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_delete.ConfirmDeleteTaskDialog
import com.example.inhabitnow.android.presentation.main.config.pick_task_progress_type.PickTaskProgressTypeDialog
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.android.presentation.view_activities.base.BaseViewTasksBuilder
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenConfig
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenEvent
import com.example.inhabitnow.android.presentation.view_activities.model.TaskFilterByStatus
import com.example.inhabitnow.android.presentation.view_activities.model.TaskSort
import com.example.inhabitnow.android.presentation.view_activities.view_habits.components.ViewHabitsScreenConfig
import com.example.inhabitnow.android.presentation.view_activities.view_habits.components.ViewHabitsScreenEvent
import com.example.inhabitnow.android.presentation.view_activities.view_habits.components.ViewHabitsScreenNavigation
import com.example.inhabitnow.android.presentation.view_activities.view_habits.components.ViewHabitsScreenState
import com.example.inhabitnow.android.presentation.view_activities.view_habits.config.view_habit_actions.ViewHabitActionsDialog
import com.example.inhabitnow.android.presentation.view_activities.view_habits.config.view_habit_actions.components.ViewHabitActionsScreenResult
import com.example.inhabitnow.android.ui.base.BaseFilterSortBuilder
import com.example.inhabitnow.android.ui.base.BaseTaskItemBuilder
import com.example.inhabitnow.android.ui.toDatePeriodDisplay
import com.example.inhabitnow.android.ui.toDisplay
import com.example.inhabitnow.domain.model.task.derived.FullTaskModel

@Composable
fun ViewHabitsScreen(
    onMenuClick: () -> Unit,
    onNavigation: (ViewHabitsScreenNavigation) -> Unit
) {
    val viewModel: ViewHabitsViewModel = hiltViewModel()
    BaseScreen(
        viewModel = viewModel,
        onNavigation = onNavigation,
        configContent = { config, onEvent ->
            ViewHabitsScreenConfigStateless(config, onEvent)
        }
    ) { state, onEvent ->
        ViewHabitsScreenStateless(
            onMenuClick = onMenuClick,
            state = state,
            onEvent = onEvent
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ViewHabitsScreenStateless(
    onMenuClick: () -> Unit,
    state: ViewHabitsScreenState,
    onEvent: (ViewHabitsScreenEvent) -> Unit
) {
    Scaffold(
        topBar = {
            ScreenTopBar(
                onMenuClick = onMenuClick,
                onSearchClick = {
                    onEvent(ViewHabitsScreenEvent.Base(BaseViewTasksScreenEvent.OnSearchClick))
                }
            )
        },
        floatingActionButton = {
            ScreenFAB(
                onClick = {
                    onEvent(ViewHabitsScreenEvent.OnCreateHabitClick)
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            BaseViewTasksBuilder.NoDataMessage(boxScope = this, result = state.allTasksResult)
            Column(modifier = Modifier.fillMaxWidth()) {
                FilterSortChipRow(
                    allSelectableTags = state.allSelectableTags,
                    filterByStatus = state.filterByStatus,
                    sort = state.sort,
                    onTagClick = { tagId ->
                        onEvent(
                            ViewHabitsScreenEvent.Base(
                                BaseViewTasksScreenEvent.OnTagClick(tagId)
                            )
                        )
                    },
                    onFilterClick = { filter ->
                        onEvent(ViewHabitsScreenEvent.OnFilterByStatusClick(filter))
                    },
                    onSortClick = { sort ->
                        onEvent(ViewHabitsScreenEvent.OnSortClick(sort))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    when (state.allTasksResult) {
                        is UIResultModel.Loading, is UIResultModel.Data -> {
                            val allHabits = state.allTasksResult.data ?: emptyList()
                            itemsIndexed(
                                items = allHabits,
                                key = { _, item -> item.taskModel.id }
                            ) { index, item ->
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    ItemHabit(
                                        item = item,
                                        onClick = {
                                            onEvent(ViewHabitsScreenEvent.OnHabitClick(item.taskModel.id))
                                        },
                                        modifier = Modifier.animateItemPlacement()
                                    )
                                    if (index != allHabits.lastIndex) {
                                        BaseTaskItemBuilder.TaskDivider()
                                    }
                                }
                            }
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemHabit(
    item: FullTaskModel.FullHabit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            TitleRow(item)
            Spacer(modifier = Modifier.height(4.dp))
            HabitDetailsRow(item)
        }
    }
}

@Composable
private fun TitleRow(item: FullTaskModel.FullHabit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = item.taskModel.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (item.allTags.isNotEmpty()) {
            BaseTaskItemBuilder.ChipTaskTags(allTags = item.allTags)
        }
    }
}

@Composable
private fun HabitDetailsRow(item: FullTaskModel.FullHabit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        BaseTaskItemBuilder.ChipTaskType(taskType = item.taskModel.type)
        BaseTaskItemBuilder.ChipTaskProgressType(taskProgressType = item.taskModel.progressType)
        BaseTaskItemBuilder.ChipTaskPriority(priority = item.taskModel.priority)
        BaseTaskItemBuilder.ChipTaskStartDate(date = item.taskModel.dateContent.startDate)
        item.taskModel.dateContent.endDate?.let { endDate ->
            BaseTaskItemBuilder.ChipTaskEndDate(date = endDate)
        }
    }
}

@Composable
private fun FilterSortChipRow(
    allSelectableTags: List<SelectableTagModel>,
    filterByStatus: TaskFilterByStatus.HabitStatus?,
    sort: TaskSort.HabitsSort?,
    onTagClick: (tagId: String) -> Unit,
    onFilterClick: (TaskFilterByStatus.HabitStatus) -> Unit,
    onSortClick: (TaskSort.HabitsSort) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            BaseViewTasksBuilder.ChipFilterByTags(
                allSelectableTags = allSelectableTags,
                onTagClick = onTagClick
            )
        }
        item {
            BaseViewTasksBuilder.ChipFilterByStatus(
                allFilters = TaskFilterByStatus.allHabitFilters,
                currentFilter = filterByStatus,
                onFilterClick = onFilterClick,
                getTextByFilter = { filter ->
                    when (filter) {
                        is TaskFilterByStatus.OnlyActive -> "Only active"
                        is TaskFilterByStatus.OnlyArchived -> "Only archived"
                    }
                }
            )
        }
        item {
            BaseViewTasksBuilder.ChipSort(
                allSort = TaskSort.allHabitsSorts,
                currentSort = sort,
                getTextBySort = { sort ->
                    when (sort) {
                        is TaskSort.ByStartDate -> "By date"
                        is TaskSort.ByPriority -> "By priority"
                        is TaskSort.ByTitle -> "By title"
                    }
                },
                onSortClick = onSortClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenTopBar(
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Habits")
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun ViewHabitsScreenConfigStateless(
    config: ViewHabitsScreenConfig,
    onResultEvent: (ViewHabitsScreenEvent) -> Unit
) {
    when (config) {
        is ViewHabitsScreenConfig.ViewHabitActions -> {
            ViewHabitActionsDialog(stateHolder = config.stateHolder) {
                onResultEvent(ViewHabitsScreenEvent.ResultEvent.ViewHabitActions(it))
            }
        }

        is ViewHabitsScreenConfig.PickTaskProgressType -> {
            PickTaskProgressTypeDialog(allTaskProgressTypes = config.allProgressTypes) {
                onResultEvent(ViewHabitsScreenEvent.ResultEvent.PickTaskProgressType(it))
            }
        }

        is ViewHabitsScreenConfig.Base -> {
            BaseViewTasksBuilder.BaseScreenConfig(config = config.baseConfig) {
                onResultEvent(
                    ViewHabitsScreenEvent.Base(
                        it
                    )
                )
            }
        }
    }
}

@Composable
private fun ScreenFAB(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = null)
    }
}







