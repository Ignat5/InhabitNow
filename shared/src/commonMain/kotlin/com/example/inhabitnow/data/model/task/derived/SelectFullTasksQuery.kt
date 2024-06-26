package com.example.inhabitnow.data.model.task.derived

data class SelectFullTasksQuery(
    public val task_id: String,
    public val task_type: String,
    public val task_progressType: String,
    public val task_title: String,
    public val task_description: String,
    public val task_startEpochDay: Long,
    public val task_endEpochDay: Long,
    public val task_priority: Long,
    public val task_createdAt: Long,
    public val task_deletedAt: Long?,
    public val taskContent_id: String,
    public val taskContent_taskId: String,
    public val taskContent_contentType: String,
    public val taskContent_content: String,
    public val taskContent_startEpochDay: Long,
    public val taskContent_createdAt: Long,
    public val reminder_id: String?,
    public val reminder_taskId: String?,
    public val reminder_time: String?,
    public val reminder_type: String?,
    public val reminder_schedule: String?,
    public val reminder_createdAt: Long?,
    public val tagCross_taskId: String?,
    public val tagCross_tagId: String?,
    public val tag_id: String?,
    public val tag_title: String?,
    public val tag_createdAt: Long?,
)
