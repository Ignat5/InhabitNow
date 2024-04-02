package com.example.inhabitnow.domain.model.statistics

sealed interface TaskStatus {
    data object Completed : TaskStatus
    sealed interface NotCompleted : TaskStatus {
        data object Pending : NotCompleted
        data object Skipped : NotCompleted
        data object Failed : NotCompleted
    }
}