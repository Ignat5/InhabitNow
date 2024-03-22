package com.example.inhabitnow.domain.use_case.tag.save_tag_cross_by_task_id

import com.example.inhabitnow.core.model.ResultModel

interface SaveTagCrossByTaskIdUseCase {
    suspend operator fun invoke(taskId: String, allTagIds: Set<String>): ResultModel<Unit>
}