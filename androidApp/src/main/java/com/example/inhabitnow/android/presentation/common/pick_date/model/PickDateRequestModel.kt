package com.example.inhabitnow.android.presentation.common.pick_date.model

import kotlinx.datetime.LocalDate

data class PickDateRequestModel(
    val currentDate: LocalDate,
    val minDate: LocalDate,
    val maxDate: LocalDate
)
