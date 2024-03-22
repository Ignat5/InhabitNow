package com.example.inhabitnow.domain.use_case.tag.update_tag_by_id

import com.example.inhabitnow.core.model.ResultModel

interface UpdateTagByIdUseCase {
    suspend operator fun invoke(
        tagId: String,
        tagTitle: String
    ): ResultModel<Unit>
}