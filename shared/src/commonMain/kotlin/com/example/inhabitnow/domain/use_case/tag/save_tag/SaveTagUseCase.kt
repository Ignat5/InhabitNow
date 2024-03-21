package com.example.inhabitnow.domain.use_case.tag.save_tag

import com.example.inhabitnow.core.model.ResultModel

interface SaveTagUseCase {
    suspend operator fun invoke(tagTitle: String): ResultModel<Unit>
}