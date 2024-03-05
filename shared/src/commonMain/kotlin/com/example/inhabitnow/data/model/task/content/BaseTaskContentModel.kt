package com.example.inhabitnow.data.model.task.content

sealed interface BaseTaskContentModel<out T : TaskContentModel> {
    val id: String
    val taskId: String
    val content: T
    val startEpochDay: Long
    val createdAt: Long
}

data class ProgressContentEntity(
    override val id: String,
    override val taskId: String,
    override val content: TaskContentModel.ProgressContent,
    override val startEpochDay: Long,
    override val createdAt: Long
) : BaseTaskContentModel<TaskContentModel.ProgressContent>

data class FrequencyContentEntity(
    override val id: String,
    override val taskId: String,
    override val content: TaskContentModel.FrequencyContent,
    override val startEpochDay: Long,
    override val createdAt: Long
) : BaseTaskContentModel<TaskContentModel.FrequencyContent>

data class ArchiveContentEntity(
    override val id: String,
    override val taskId: String,
    override val content: TaskContentModel.ArchiveContent,
    override val startEpochDay: Long,
    override val createdAt: Long
) : BaseTaskContentModel<TaskContentModel.ArchiveContent>