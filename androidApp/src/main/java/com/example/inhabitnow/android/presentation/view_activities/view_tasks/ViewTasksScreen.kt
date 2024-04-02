package com.example.inhabitnow.android.presentation.view_activities.view_tasks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.model.SelectableTagModel
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.android.presentation.view_activities.base.BaseViewTasksBuilder
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenEvent
import com.example.inhabitnow.android.presentation.view_activities.model.TaskFilterByStatus
import com.example.inhabitnow.android.presentation.view_activities.model.TaskSort
import com.example.inhabitnow.android.presentation.view_activities.view_tasks.components.ViewTasksScreenEvent
import com.example.inhabitnow.android.presentation.view_activities.view_tasks.components.ViewTasksScreenNavigation
import com.example.inhabitnow.android.presentation.view_activities.view_tasks.components.ViewTasksScreenState
import com.example.inhabitnow.android.ui.base.BaseTaskItemBuilder
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import com.example.inhabitnow.domain.model.task.derived.FullTaskModel

@Composable
fun ViewTasksScreen(
    onMenuClick: () -> Unit,
    onNavigation: (ViewTasksScreenNavigation) -> Unit
) {
    val viewModel: ViewTasksViewModel = hiltViewModel()
    BaseScreen(
        viewModel = viewModel,
        onNavigation = onNavigation,
        configContent = { config, onEvent ->

        }
    ) { state, onEvent ->
        ViewTasksScreenStateless(onMenuClick, state, onEvent)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ViewTasksScreenStateless(
    onMenuClick: () -> Unit,
    state: ViewTasksScreenState,
    onEvent: (ViewTasksScreenEvent) -> Unit
) {
    Scaffold(
        topBar = {
            ScreenTopBar(
                onMenuClick = onMenuClick,
                onSearchClick = {
                    onEvent(
                        ViewTasksScreenEvent.Base(
                            BaseViewTasksScreenEvent.OnSearchClick
                        )
                    )
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            BaseViewTasksBuilder.NoDataMessage(
                boxScope = this,
                result = state.allTasksResult
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                FilterSortChipRow(
                    allSelectableTags = state.allSelectableTags,
                    filterByStatus = state.filterByStatus,
                    sort = state.sort,
                    onTagClick = { tagId ->
                        onEvent(
                            ViewTasksScreenEvent.Base(
                                BaseViewTasksScreenEvent.OnTagClick(tagId)
                            )
                        )
                    },
                    onFilterClick = { filter ->
                        onEvent(ViewTasksScreenEvent.OnFilterByStatusClick(filter))
                    },
                    onSortClick = { sort ->
                        onEvent(ViewTasksScreenEvent.OnSortClick(sort))
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                val itemsData = remember(state.allTasksResult) {
                    state.allTasksResult.data ?: emptyList()
                }
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    itemsIndexed(
                        items = itemsData,
                        key = { _, item -> item.taskModel.id }
                    ) { index, item ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            ItemTask(
                                item = item,
                                onClick = {
                                    onEvent(ViewTasksScreenEvent.OnTaskClick(item.taskModel.id))
                                },
                                modifier = Modifier.animateItemPlacement()
                            )
                            if (index != itemsData.lastIndex) {
                                BaseTaskItemBuilder.TaskDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemTask(
    item: FullTaskModel.FullTask,
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
private fun TitleRow(item: FullTaskModel.FullTask) {
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
private fun HabitDetailsRow(item: FullTaskModel.FullTask) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        BaseTaskItemBuilder.ChipTaskType(taskType = item.taskModel.type)
        BaseTaskItemBuilder.ChipTaskProgressType(taskProgressType = item.taskModel.progressType)
        BaseTaskItemBuilder.ChipTaskPriority(priority = item.taskModel.priority)
        when (val dc = item.taskModel.dateContent) {
            is TaskContentModel.DateContent.Day -> {
                BaseTaskItemBuilder.ChipTaskStartDate(date = dc.date)
            }

            is TaskContentModel.DateContent.Period -> {
                BaseTaskItemBuilder.ChipTaskStartDate(date = dc.startDate)
                dc.endDate?.let { endDate ->
                    BaseTaskItemBuilder.ChipTaskEndDate(date = endDate)
                }
            }
        }
    }
}

@Composable
private fun FilterSortChipRow(
    allSelectableTags: List<SelectableTagModel>,
    filterByStatus: TaskFilterByStatus.TaskStatus?,
    sort: TaskSort.TasksSort?,
    onTagClick: (tagId: String) -> Unit,
    onFilterClick: (TaskFilterByStatus.TaskStatus) -> Unit,
    onSortClick: (TaskSort.TasksSort) -> Unit,
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
                allFilters = TaskFilterByStatus.allTasksFilters,
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
                allSort = TaskSort.allTasksSorts,
                currentSort = sort,
                onSortClick = onSortClick,
                getTextBySort = { sort ->
                    when (sort) {
                        is TaskSort.ByStartDate -> "By date"
                        is TaskSort.ByPriority -> "By priority"
                        is TaskSort.ByTitle -> "By title"
                    }
                }
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
            Text(text = "Tasks")
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

