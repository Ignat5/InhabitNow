package com.example.inhabitnow.data.model.task.content.base

interface BaseTaskContentEntity<out T : TaskContentEntity> {
    val id: String
    val taskId: String
    val content: T
    val startEpochDay: Long
    val createdAt: Long
}