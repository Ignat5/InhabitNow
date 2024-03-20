package com.example.inhabitnow.android.presentation.view_tags

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.android.presentation.view_tags.components.ViewTagsScreenConfig
import com.example.inhabitnow.android.presentation.view_tags.components.ViewTagsScreenEvent
import com.example.inhabitnow.android.presentation.view_tags.components.ViewTagsScreenNavigation
import com.example.inhabitnow.android.presentation.view_tags.components.ViewTagsScreenState
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.create.CreateTagDialog
import com.example.inhabitnow.domain.model.tag.TagModel

@Composable
fun ViewTagsScreen(
    onNavigation: (ViewTagsScreenNavigation) -> Unit
) {
    val viewModel: ViewTagsViewModel = hiltViewModel()
    BaseScreen(
        viewModel = viewModel,
        onNavigation = onNavigation,
        configContent = { config, onEvent ->
            ScreenConfigStateless(config, onEvent)
        }) { state, onEvent ->
        ViewTagsScreenStateless(state, onEvent)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ViewTagsScreenStateless(
    state: ViewTagsScreenState,
    onEvent: (ViewTagsScreenEvent) -> Unit
) {
    BackHandler { onEvent(ViewTagsScreenEvent.OnBackRequest) }
    Scaffold(
        topBar = {
            ScreenTopBar(
                onBackClick = { onEvent(ViewTagsScreenEvent.OnBackRequest) }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                when (state.result) {
                    is UIResultModel.Loading, is UIResultModel.Data -> {
                        items(
                            items = state.result.data ?: emptyList(),
                            key = { tag -> tag.id },
                            contentType = { ScreenItemContentType.Item }
                        ) { item ->
                            ItemTag(
                                tagModel = item,
                                onClick = {
                                    onEvent(ViewTagsScreenEvent.OnTagClick(item.id))
                                },
                                onDeleteTagClick = {
                                    onEvent(ViewTagsScreenEvent.OnDeleteTagClick(item.id))
                                },
                                modifier = Modifier.animateItemPlacement()
                            )
                        }
                    }

                    is UIResultModel.NoData -> {
                        item(
                            key = ScreenItemContentType.Message,
                            contentType = ScreenItemContentType.Message
                        ) {
                            NoTagsMessage(
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                )
                            )
                        }
                    }
                }
                item(
                    key = ScreenItemContentType.AddButton,
                    contentType = ScreenItemContentType.AddButton
                ) {
                    AddTagButton(
                        onClick = {
                            onEvent(ViewTagsScreenEvent.OnCreateTagClick)
                        },
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ScreenConfigStateless(
    config: ViewTagsScreenConfig,
    onEvent: (ViewTagsScreenEvent.ResultEvent) -> Unit
) {
    when (config) {
        is ViewTagsScreenConfig.CreateTag -> {
            CreateTagDialog(
                stateHolder = config.stateHolder,
                onResult = {
                    onEvent(ViewTagsScreenEvent.ResultEvent.CreateTag(it))
                }
            )
        }
    }
}

@Composable
private fun ItemTag(
    tagModel: TagModel,
    onClick: () -> Unit,
    onDeleteTagClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_tag),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = tagModel.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDeleteTagClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun AddTagButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "New tag",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun NoTagsMessage(modifier: Modifier = Modifier) {
    Text(
        text = "You have no tags",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
            .fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenTopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Tags")
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null
                )
            }
        }
    )
}

private enum class ScreenItemContentType {
    Message,
    Item,
    AddButton
}