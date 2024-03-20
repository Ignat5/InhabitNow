package com.example.inhabitnow.domain.use_case.tag.save_tag

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.core.util.randomUUID
import com.example.inhabitnow.data.model.tag.TagEntity
import com.example.inhabitnow.data.repository.tag.TagRepository
import kotlinx.datetime.Clock

class DefaultSaveTagUseCase(
    private val tagRepository: TagRepository
) : SaveTagUseCase {

    override suspend operator fun invoke(tagTitle: String): ResultModel<Unit> =
        tagRepository.saveTag(
            TagEntity(
                id = randomUUID(),
                title = tagTitle,
                createdAt = Clock.System.now().toEpochMilliseconds()
            )
        )

}