package com.example.inhabitnow.domain.use_case.tag.delete_tag

import com.example.inhabitnow.core.model.ResultModel

interface DeleteTagByIdUseCase {
    suspend operator fun invoke(tagId: String): ResultModel<Unit>
}