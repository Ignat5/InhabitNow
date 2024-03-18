package com.example.inhabitnow.android.presentation.model

sealed class UIResultModel<out T : Any>(open val data: T?) {
    data class Data<out T : Any>(override val data: T) : UIResultModel<T>(data)
    data class Loading<out T : Any>(override val data: T?) : UIResultModel<T>(data)
    data object NoData : UIResultModel<Nothing>(null)
}