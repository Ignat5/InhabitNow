package com.example.inhabitnow.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.example.inhabitnow.android.navigation.root.RootGraph
import com.example.inhabitnow.android.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                RootGraph()
//                Dialog(
//                    onDismissRequest = {}
//                ) {
//                    val baseHeight = TextFieldDefaults.MinHeight * 2
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Box(
//                            contentAlignment = Alignment.Center,
//                            modifier = Modifier
//                                .height(baseHeight)
//                                .weight(1f)
//                        ) {
//                            Box(
//                                modifier = Modifier
//                                    .align(Alignment.Center)
//                                    .fillMaxWidth()
//                                    .height(baseHeight / 3)
//                                    .border(
//                                        1.dp,
//                                        MaterialTheme.colorScheme.outline,
//                                        RoundedCornerShape(4.dp)
//                                    )
//                            )
//                            val seed = 1_000_000 * 24 - 1
//                            val lazyListState =
//                                rememberLazyListState(initialFirstVisibleItemIndex = seed)
//                            val flingBehavior =
//                                rememberSnapFlingBehavior(lazyListState = lazyListState)
//                            var currentItemIndex by remember {
//                                mutableIntStateOf((lazyListState.firstVisibleItemIndex + 1) % 24)
//                            }
//                            LazyColumn(
//                                modifier = Modifier
//                                    .fillMaxSize(),
//                                verticalArrangement = Arrangement.Center,
//                                state = lazyListState,
//                                flingBehavior = flingBehavior
//                            ) {
//                                items(Int.MAX_VALUE) {
//                                    val item = it % 24
//                                    val isCurrent = remember(currentItemIndex) {
//                                        item == currentItemIndex
//                                    }
//                                    Box(
//                                        modifier = Modifier
//                                            .height(baseHeight / 3),
//                                        contentAlignment = Alignment.Center
//                                    ) {
//                                        Text(
//                                            text = if (item <= 9) "0$item" else "$item",
//                                            modifier = Modifier
//                                                .fillMaxWidth()
//                                                .alpha(if (isCurrent) 1f else 0.5f),
//                                            textAlign = TextAlign.Center,
//                                            style = MaterialTheme.typography.titleLarge,
//                                            color = MaterialTheme.colorScheme.onSurface
//                                        )
//                                    }
//                                }
//                            }
//                            LaunchedEffect(key1 = lazyListState.isScrollInProgress) {
//                                val isScrolling = lazyListState.isScrollInProgress
//                                if (!isScrolling) {
//                                    currentItemIndex =
//                                        (lazyListState.firstVisibleItemIndex + 1) % 24
//                                }
//                            }
//                        }
//
//                        Text(
//                            text = ":",
//                            style = MaterialTheme.typography.titleLarge,
//                            color = MaterialTheme.colorScheme.onSurface,
//                            modifier = Modifier.padding(horizontal = 8.dp)
//                        )
//
//                        Box(
//                            contentAlignment = Alignment.Center,
//                            modifier = Modifier
//                                .height(baseHeight)
//                                .weight(1f)
//                        ) {
//                            Box(
//                                modifier = Modifier
//                                    .align(Alignment.Center)
//                                    .fillMaxWidth()
//                                    .height(baseHeight / 3)
//                                    .border(
//                                        1.dp,
//                                        MaterialTheme.colorScheme.outline,
//                                        RoundedCornerShape(4.dp)
//                                    )
//                            )
//                            val seed = 1_000_000 * 60 - 1
//                            val lazyListState =
//                                rememberLazyListState(initialFirstVisibleItemIndex = seed)
//                            val flingBehavior =
//                                rememberSnapFlingBehavior(lazyListState = lazyListState)
//                            var currentItemIndex by remember {
//                                mutableIntStateOf((lazyListState.firstVisibleItemIndex + 1) % 60)
//                            }
//                            LazyColumn(
//                                modifier = Modifier
//                                    .fillMaxSize(),
//                                verticalArrangement = Arrangement.Center,
//                                state = lazyListState,
//                                flingBehavior = flingBehavior
//                            ) {
//                                items(Int.MAX_VALUE) {
//                                    val item = it % 60
//                                    val isCurrent = remember(currentItemIndex) {
//                                        item == currentItemIndex
//                                    }
//                                    Box(
//                                        modifier = Modifier
//                                            .height(baseHeight / 3),
//                                        contentAlignment = Alignment.Center
//                                    ) {
//                                        Text(
//                                            text = if (item <= 9) "0$item" else "$item",
//                                            modifier = Modifier
//                                                .fillMaxWidth()
//                                                .alpha(if (isCurrent) 1f else 0.5f),
//                                            textAlign = TextAlign.Center,
//                                            style = MaterialTheme.typography.titleLarge,
//                                            color = MaterialTheme.colorScheme.onSurface
//                                        )
//                                    }
//                                }
//                            }
//                            LaunchedEffect(key1 = lazyListState.isScrollInProgress) {
//                                val isScrolling = lazyListState.isScrollInProgress
//                                if (!isScrolling) {
//                                    currentItemIndex =
//                                        (lazyListState.firstVisibleItemIndex + 1) % 60
//                                }
//                            }
//                        }
//                    }
//                }
            }
        }
    }
}