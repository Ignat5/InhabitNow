package com.example.inhabitnow.core.model

sealed interface ResultModel<out T : Any> {
    val data: T?

    data class Success<T : Any>(override val data: T) : ResultModel<T>
    data class Error<T : Any>(
        val throwable: Throwable,
        override val data: T? = null
    ) : ResultModel<T>
}

sealed interface ResultModelWithException<out T : Any, out E : Exception> {
    val data: T?

    data class Success<T : Any, out E : Exception>(override val data: T) : ResultModelWithException<T, E>
    data class Error<T : Any, out E : Exception>(
        val exception: E,
        override val data: T? = null
    ) : ResultModelWithException<T, E>
}