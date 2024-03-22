package com.example.inhabitnow.android.presentation.search_tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.search_tasks.components.SearchTasksScreenEvent
import com.example.inhabitnow.android.presentation.search_tasks.components.SearchTasksScreenNavigation
import com.example.inhabitnow.android.presentation.search_tasks.components.SearchTasksScreenState
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTasksScreenStateless(
    state: SearchTasksScreenState,
    onEvent: (SearchTasksScreenEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Box(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = state.searchQuery,
            onQueryChange = { onEvent(SearchTasksScreenEvent.OnInputUpdateSearchQuery(it)) },
            onSearch = { focusManager.clearFocus() },
            active = true,
            onActiveChange = {},
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
            }
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(
                    items = state.allTasksWithContent,
                    key = { it.task.id }
                ) {

                }
            }
        }
    }
}

@Composable
private fun ItemTask(
    item: TaskWithContentModel,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

        }
    }
}