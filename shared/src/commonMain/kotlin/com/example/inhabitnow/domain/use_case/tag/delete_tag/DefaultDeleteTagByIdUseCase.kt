package com.example.inhabitnow.domain.use_case.tag.delete_tag

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.repository.tag.TagRepository

class DefaultDeleteTagByIdUseCase(
    private val tagRepository: TagRepository
) : DeleteTagByIdUseCase {

    override suspend operator fun invoke(tagId: String): ResultModel<Unit> =
        tagRepository.deleteTagById(tagId)

}