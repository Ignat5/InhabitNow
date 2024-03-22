package com.example.inhabitnow.android.presentation.common.pick_date

import com.example.inhabitnow.android.presentation.base.state_holder.BaseResultStateHolder
import com.example.inhabitnow.android.presentation.common.pick_date.components.PickDateScreenEvent
import com.example.inhabitnow.android.presentation.common.pick_date.components.PickDateScreenResult
import com.example.inhabitnow.android.presentation.common.pick_date.components.PickDateScreenState
import com.example.inhabitnow.android.presentation.common.pick_date.model.PickDateRequestModel
import com.example.inhabitnow.android.presentation.common.pick_date.model.UIDateItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class PickDateStateHolder(
    private val requestModel: PickDateRequestModel,
    override val holderScope: CoroutineScope,
    private val defaultDispatcher: CoroutineDispatcher
) : BaseResultStateHolder<PickDateScreenEvent, PickDateScreenState, PickDateScreenResult>() {
    private val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    private val currentDateState = MutableStateFlow(requestModel.currentDate.firstDayOfMonth)
    private val currentPickedDateState = MutableStateFlow(requestModel.currentDate)
    private val allDaysOfMonthState = currentDateState.map { currentDate ->
        withContext(defaultDispatcher) {
            currentDate.provideDateItems()
        }
    }.stateIn(
        holderScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    override val uiScreenState: StateFlow<PickDateScreenState> = combine(
        currentDateState, allDaysOfMonthState, currentPickedDateState
    ) { currentDate, allDaysOfMonth, currentPickedDate ->
        PickDateScreenState(
            currentDate = currentDate,
            allDaysOfMonth = allDaysOfMonth,
            currentPickedDate = currentPickedDate,
            todayDate = todayDate
        )
    }.stateIn(
        holderScope,
        SharingStarted.Eagerly,
        PickDateScreenState(
            currentDate = currentDateState.value,
            allDaysOfMonth = allDaysOfMonthState.value,
            currentPickedDate = currentPickedDateState.value,
            todayDate = todayDate
        )
    )

    override fun onEvent(event: PickDateScreenEvent) {
        when (event) {
            is PickDateScreenEvent.OnDateItemClick -> onDateItemClick(event)
            is PickDateScreenEvent.OnNextMonthClick -> onNextMonthClick()
            is PickDateScreenEvent.OnPrevMonthClick -> onPrevMonthClick()
            is PickDateScreenEvent.OnConfirmClick -> onConfirmClick()
            is PickDateScreenEvent.OnDismissRequest -> onDismissRequest()
        }
    }

    private fun onDateItemClick(event: PickDateScreenEvent.OnDateItemClick) {
        event.date.let { date ->
            if (date in requestModel.minDate..requestModel.maxDate) {
                currentPickedDateState.update {
                    date
                }
            }
        }
    }

    private fun onNextMonthClick() {
        currentDateState.update { oldDate ->
            oldDate.plus(1, DateTimeUnit.MONTH)
        }
    }

    private fun onPrevMonthClick() {
        currentDateState.update { oldDate ->
            oldDate.minus(1, DateTimeUnit.MONTH)
        }
    }

    private fun onConfirmClick() {
        setUpResult(
            PickDateScreenResult.Confirm(
                date = currentPickedDateState.value
            )
        )
    }

    private fun onDismissRequest() {
        setUpResult(PickDateScreenResult.Dismiss)
    }

    private fun LocalDate.provideDateItems(): List<UIDateItem> =
        this.firstDayOfMonth.let { currentDate ->
            val result = mutableListOf<UIDateItem>()
            // add days of previous month (starting Monday)
            currentDate.dayOfWeek.ordinal.let { diff ->
                currentDate.minus(diff, DateTimeUnit.DAY).let { startDate ->
                    var nextDate = startDate
                    while (nextDate < currentDate) {
                        result.add(UIDateItem.UnPickAble.OtherMonth(nextDate))
                        nextDate = nextDate.plus(1, DateTimeUnit.DAY)
                    }
                }
            }
            // add days of current month
            currentDate.plus(1, DateTimeUnit.MONTH).let { nextMonthDate ->
                currentDate.daysUntil(nextMonthDate).let { daysInMonth ->
                    val monthRange = FIRST_DAY_OF_MONTH..daysInMonth
                    val passMin = currentDate >= requestModel.minDate
                    val passMax = nextMonthDate <= requestModel.maxDate
                    if (passMin && passMax) {
                        result.addAll(
                            monthRange.map { dayOfMonth ->
                                UIDateItem.PickAble.Day(
                                    currentDate.copy(dayOfMonth = dayOfMonth)
                                )
                            }
                        )
                    } else {
                        result.addAll(
                            monthRange.map { nextDayOfMonth ->
                                currentDate.copy(dayOfMonth = nextDayOfMonth).let { nextDate ->
                                    (nextDate !in requestModel.minDate..requestModel.maxDate).let { isLocked ->
                                        if (isLocked) UIDateItem.UnPickAble.Locked(nextDate)
                                        else UIDateItem.PickAble.Day(nextDate)
                                    }
                                }
                            }
                        )
                    }
                }
            }
            result
        }

    private val LocalDate.firstDayOfMonth
        get() = this.copy(dayOfMonth = FIRST_DAY_OF_MONTH)

    private fun LocalDate.copy(
        year: Int? = null,
        monthNumber: Int? = null,
        dayOfMonth: Int? = null
    ) = this.let { oldDate ->
        LocalDate(
            year = year ?: oldDate.year,
            monthNumber = monthNumber ?: oldDate.monthNumber,
            dayOfMonth = dayOfMonth ?: oldDate.dayOfMonth
        )
    }

    companion object {
        private const val FIRST_DAY_OF_MONTH = 1
    }

}