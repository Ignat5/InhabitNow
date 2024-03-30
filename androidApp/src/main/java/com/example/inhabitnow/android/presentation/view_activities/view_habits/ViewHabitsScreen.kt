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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.view_activities.model.TaskFilterByStatus
import com.example.inhabitnow.android.presentation.view_activities.view_habits.components.ViewHabitsScreenConfig
import com.example.inhabitnow.android.presentation.view_activities.view_habits.components.ViewHabitsScreenEvent
import com.example.inhabitnow.android.presentation.view_activities.view_habits.components.ViewHabitsScreenNavigation
import com.example.inhabitnow.android.presentation.view_activities.view_habits.components.ViewHabitsScreenState
import com.example.inhabitnow.android.ui.base.BaseFilterSortBuilder
import com.example.inhabitnow.android.ui.base.BaseTaskItemBuilder
import com.example.inhabitnow.android.ui.toDatePeriodDisplay
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

                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        BaseFilterSortBuilder.ChipFilterByTags(
                            allTags = state.allTags,
                            filterByTagsIds = state.filterByTagsIds,
                            onTagClick = { tagId ->
                                onEvent(ViewHabitsScreenEvent.OnFilterTagClick(tagId))
                            }
                        )
                    }
                    item {
                        BaseFilterSortBuilder.ChipFilterByStatus(
                            currentFilter = state.filterByStatus,
                            allFilters = TaskFilterByStatus.allHabitFilters,
                            onFilterClick = { filter ->
                                onEvent(ViewHabitsScreenEvent.OnFilterByStatusClick(filter))
                            }
                        )
                    }
                }
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    itemsIndexed(
                        items = state.allHabits,
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
                            if (index != state.allHabits.lastIndex) {
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
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.taskModel.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = item.taskModel.dateContent.toDatePeriodDisplay(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
        if (item.allTags.isNotEmpty()) {
            BaseTaskItemBuilder.ChipTaskTags(allTags = item.allTags)
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

}