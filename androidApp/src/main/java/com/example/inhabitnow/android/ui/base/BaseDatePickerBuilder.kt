package com.example.inhabitnow.android.ui.base

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.common.pick_date.model.UIDateItem
import com.example.inhabitnow.android.ui.toDisplay
import com.example.inhabitnow.android.ui.toMonthYear
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

private const val GRID_COLUMN_COUNT = 7

object BaseDatePickerBuilder {
    @Composable
    fun MonthController(
        date: LocalDate,
        onPrevClick: () -> Unit,
        onNextClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val monthYear = remember(date) {
            date.toMonthYear()
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPrevClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_previous),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
            Text(
                text = monthYear,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onNextClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_next),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        }
    }

    @Composable
    fun MonthGrid(
        allDaysOfMonth: List<UIDateItem>,
        currentPickedDate: LocalDate,
        currentDate: LocalDate,
        todayDate: LocalDate,
        onDayOfMonthClick: (LocalDate) -> Unit
    ) {
        val allDaysOfWeek = remember { DayOfWeek.entries }
        LazyVerticalGrid(
            columns = GridCells.Fixed(GRID_COLUMN_COUNT),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items = allDaysOfWeek,
                key = { it },
                contentType = { ItemContentType.DayOfWeekLabel }
            ) { itemDayOfWeek ->
                ItemDayOfWeek(dayOfWeek = itemDayOfWeek)
            }
            items(
                items = allDaysOfMonth,
                key = { it.date.toEpochDays() },
                contentType = { ItemContentType.DayOfMonth }
            ) { item ->
                val isPicked = remember(currentPickedDate) {
                    item.date == currentPickedDate
                }
                val isToday = remember(todayDate) {
                    item.date == todayDate
                }
                ItemDayOfMonth(
                    dateItem = item,
                    isPicked = isPicked,
                    isToday = isToday,
                    onClick = {
                        onDayOfMonthClick(item.date)
                    }
                )
            }
        }
    }

    @Composable
    private fun ItemDayOfMonth(
        dateItem: UIDateItem,
        isPicked: Boolean,
        isToday: Boolean,
        onClick: () -> Unit
    ) {
        val isEnabled = remember(dateItem) {
            dateItem !is UIDateItem.UnPickAble
        }
        val isShown = remember(dateItem) {
            dateItem !is UIDateItem.UnPickAble.OtherMonth
        }
        val label = remember(dateItem) {
            "${dateItem.date.dayOfMonth}"
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
//                .padding(horizontal = 2.dp)
                .clip(MaterialTheme.shapes.extraLarge)
                .then(
                    Modifier.alpha(if (isShown) 1f else 0f)
                )
                .then(
                    if (isPicked) {
                        Modifier.background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.extraLarge
                        )
                    } else if (isToday) {
                        Modifier.border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.extraLarge
                        )
                    } else Modifier
                )
                .clickable(
                    enabled = isEnabled,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isPicked) {
                    MaterialTheme.colorScheme.onPrimary
                } else if (isToday) {
                    MaterialTheme.colorScheme.primary
                } else if (!isEnabled) MaterialTheme.colorScheme.outline
                else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    private fun ItemDayOfWeek(
        dayOfWeek: DayOfWeek,
        modifier: Modifier = Modifier
    ) {
        val label = remember { dayOfWeek.toDisplay().take(1) }
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }

    private enum class ItemContentType {
        DayOfWeekLabel,
        DayOfMonth
    }
}

//@Composable
//fun BaseDatePicker(
//    currentDate: LocalDate,
//    currentPickedDate: LocalDate,
//    allDaysOfMonth: List<UIDateItem>,
//    todayDate: LocalDate,
//    onDayOfMonthClick: (Int) -> Unit,
//    onNextMonthClick: () -> Unit,
//    onPrevMonthClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Column(modifier = modifier.fillMaxWidth()) {
//        MonthController(
//            date = currentDate,
//            onPrevClick = { /*TODO*/ },
//            onNextClick = { /*TODO*/ }
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        MonthGrid(
//            allDaysOfMonth = allDaysOfMonth,
//            currentDate = currentDate,
//            currentPickedDate = currentPickedDate,
//            todayDate = todayDate,
//            onDayOfMonthClick = onDayOfMonthClick
//        )
//    }
//}






