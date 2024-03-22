package com.example.inhabitnow.android.presentation.common.pick_date.model

import kotlinx.datetime.LocalDate

sealed interface UIDateItem {
    val date: LocalDate

    sealed interface PickAble : UIDateItem {
        data class Day(override val date: LocalDate) : PickAble
    }

    sealed interface UnPickAble : UIDateItem {
        data class OtherMonth(override val date: LocalDate) : UnPickAble
        data class Locked(override val date: LocalDate) : UnPickAble
    }
}