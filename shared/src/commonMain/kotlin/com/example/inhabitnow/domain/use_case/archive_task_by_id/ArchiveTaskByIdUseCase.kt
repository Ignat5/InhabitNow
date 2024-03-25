package com.example.inhabitnow.domain.use_case.archive_task_by_id

import com.example.inhabitnow.core.model.ResultModel

interface ArchiveTaskByIdUseCase {
    suspend operator fun invoke(taskId: String, archive: Boolean): ResultModel<Unit>
}