package com.example.inhabitnow.domain.use_case.tag.update_tag_by_id

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.repository.tag.TagRepository

class DefaultUpdateTagByIdUseCase(
    private val tagRepository: TagRepository
) : UpdateTagByIdUseCase {

    override suspend operator fun invoke(
        tagId: String,
        tagTitle: String
    ): ResultModel<Unit> = tagRepository.updateTagById(
        tagId = tagId,
        tagTitle = tagTitle
    )

}