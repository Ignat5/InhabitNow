package com.example.inhabitnow.domain.use_case.tag.save_tag_cross_by_task_id

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.repository.tag.TagRepository

class DefaultSaveTagCrossByTaskIdUseCase(
    private val tagRepository: TagRepository
) : SaveTagCrossByTaskIdUseCase {

    override suspend operator fun invoke(
        taskId: String,
        allTagIds: Set<String>
    ): ResultModel<Unit> =
        tagRepository.saveTagCrossByTaskId(taskId = taskId, allTagIds = allTagIds)

}