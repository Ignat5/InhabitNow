package com.example.inhabitnow.android.ui.base

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.common.pick_date.model.UIDateItem
import com.example.inhabitnow.android.ui.theme.AppTheme
import com.example.inhabitnow.android.ui.toDayOfWeekMonthDayOfMonth
import com.example.inhabitnow.android.ui.toDisplay
import com.example.inhabitnow.android.ui.toMonthYear
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

private const val DEFAULT_SCREEN_FRACTION = 0.75f
private const val GRID_COLUMN_COUNT = 7

@Composable
fun BaseDatePicker(
    currentDate: LocalDate,
    currentPickedDate: LocalDate,
    allDaysOfMonth: List<UIDateItem>,
    todayDate: LocalDate,
    onDayOfMonthClick: (Int) -> Unit,
    onNextMonthClick: () -> Unit,
    onPrevMonthClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        MonthController(
            date = currentDate,
            onPrevClick = { /*TODO*/ },
            onNextClick = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.height(16.dp))
        MonthGrid(
            allDaysOfMonth = allDaysOfMonth,
            currentDate = currentDate,
            currentPickedDate = currentPickedDate,
            todayDate = todayDate,
            onDayOfMonthClick = onDayOfMonthClick
        )
    }
}

@Composable
private fun MonthGrid(
    allDaysOfMonth: List<UIDateItem>,
    currentDate: LocalDate,
    currentPickedDate: LocalDate,
    todayDate: LocalDate,
    onDayOfMonthClick: (Int) -> Unit
) {
    val allDaysOfWeek = remember { DayOfWeek.entries }
    val containsPickedDate = remember(currentDate, currentPickedDate) {
        currentPickedDate.month == currentDate.month &&
                currentPickedDate.year == currentDate.year
    }
    val containsTodayDate = remember(currentDate, todayDate) {
        todayDate.month == currentDate.month &&
                todayDate.year == currentDate.year
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(GRID_COLUMN_COUNT),
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
            key = { it.dayOfMonth },
            contentType = { ItemContentType.DayOfMonth }
        ) { item ->
            val isPicked = remember(containsPickedDate, currentPickedDate) {
                if (containsPickedDate) item.dayOfMonth == currentPickedDate.dayOfMonth
                else false
            }
            val isToday = remember(containsTodayDate, todayDate) {
                if (containsTodayDate) item.dayOfMonth == todayDate.dayOfMonth
                else false
            }
            ItemDayOfMonth(
                dateItem = item,
                isPicked = isPicked,
                isToday = isToday,
                onClick = {
                    onDayOfMonthClick(item.dayOfMonth)
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
        "${dateItem.dayOfMonth}"
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.extraLarge)
            .then(
                if (!isShown) {
                    Modifier.alpha(1f)
                } else if (isPicked) {
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

@Composable
private fun MonthController(
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


//@Composable
//@Preview
//private fun LocalPreview() {
//    val today = LocalDate(year = 2024, monthNumber = 3, dayOfMonth = 21)
//    val allDaysOfMonth = IntRange(1, 31).toList()
//    AppTheme {
//        BaseDatePicker(
//            currentDate = today,
//            allDaysOfMonth = allDaysOfMonth
//        )
//    }
//}





