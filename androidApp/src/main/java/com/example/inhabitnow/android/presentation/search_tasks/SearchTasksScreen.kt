package com.example.inhabitnow.android.presentation.search_tasks

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.search_tasks.components.SearchTasksScreenEvent
import com.example.inhabitnow.android.presentation.search_tasks.components.SearchTasksScreenNavigation
import com.example.inhabitnow.android.presentation.search_tasks.components.SearchTasksScreenState
import com.example.inhabitnow.android.ui.toDatePeriodDisplay
import com.example.inhabitnow.android.ui.toDisplay
import com.example.inhabitnow.domain.model.task.TaskWithContentModel

@Composable
fun SearchTasksScreen(onNavigation: (SearchTasksScreenNavigation) -> Unit) {
    val viewModel: SearchTasksViewModel = hiltViewModel()
    BaseScreen(
        viewModel = viewModel,
        onNavigation = onNavigation,
        configContent = { _, _ -> }
    ) { state, onEvent ->
        SearchTasksScreenStateless(state, onEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun SearchTasksScreenStateless(
    state: SearchTasksScreenState,
    onEvent: (SearchTasksScreenEvent) -> Unit
) {
    BackHandler { onEvent(SearchTasksScreenEvent.OnBackRequest) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    Box(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = state.searchQuery,
            onQueryChange = { onEvent(SearchTasksScreenEvent.OnInputUpdateSearchQuery(it)) },
            onSearch = { focusManager.clearFocus() },
            active = true,
            onActiveChange = {},
            leadingIcon = {
                IconButton(onClick = { onEvent(SearchTasksScreenEvent.OnBackRequest) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null
                    )
                }
            },
            trailingIcon = {
                if (state.canClearSearch) {
                    IconButton(onClick = { onEvent(SearchTasksScreenEvent.OnClearSearchClick) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = null
                        )
                    }
                }
            },
            placeholder = {
                Text(text = "Search...")
            },
            modifier = Modifier
                .focusRequester(focusRequester)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(
                    items = state.allTasksWithContent,
                    key = { it.task.id }
                ) { item ->
                    ItemTask(
                        item = item,
                        onClick = {
                            onEvent(SearchTasksScreenEvent.OnTaskClick(item.task.id))
                        },
                        modifier = Modifier.animateItemPlacement()
                    )
                }
            }
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

@Composable
private fun ItemTask(
    item: TaskWithContentModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = item.task.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = item.task.toDatePeriodDisplay(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = item.task.type.toDisplay(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}