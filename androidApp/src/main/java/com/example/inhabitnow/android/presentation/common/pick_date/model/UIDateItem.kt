package com.example.inhabitnow.android.presentation.common.pick_date.model

sealed interface UIDateItem {
    val dayOfMonth: Int

    sealed interface PickAble : UIDateItem {
        data class Day(override val dayOfMonth: Int) : PickAble
//        data class Today(override val dayOfMonth: Int) : PickAble
//        data class Current(override val dayOfMonth: Int) : PickAble
    }

    sealed interface UnPickAble : UIDateItem {
        data class OtherMonth(override val dayOfMonth: Int) : UnPickAble
        data class Locked(override val dayOfMonth: Int) : UnPickAble
    }
}