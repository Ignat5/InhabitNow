package com.example.inhabitnow.core.model

sealed interface ResultModel<out T : Any> {
    val data: T?

    data class Success<T : Any>(override val data: T) : ResultModel<T>
    data class Error<T : Any>(
        val throwable: Throwable,
        override val data: T? = null
    ) : ResultModel<T>
}