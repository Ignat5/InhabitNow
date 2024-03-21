package com.example.inhabitnow.android.core.di.module.domain.tag

import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.data.repository.tag.TagRepository
import com.example.inhabitnow.domain.use_case.tag.delete_tag.DefaultDeleteTagByIdUseCase
import com.example.inhabitnow.domain.use_case.tag.delete_tag.DeleteTagByIdUseCase
import com.example.inhabitnow.domain.use_case.tag.read_tag_ids_by_task_id.DefaultReadTagIdsByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.tag.read_tag_ids_by_task_id.ReadTagIdsByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.tag.read_tags.DefaultReadTagsUseCase
import com.example.inhabitnow.domain.use_case.tag.read_tags.ReadTagsUseCase
import com.example.inhabitnow.domain.use_case.tag.save_tag.DefaultSaveTagUseCase
import com.example.inhabitnow.domain.use_case.tag.save_tag.SaveTagUseCase
import com.example.inhabitnow.domain.use_case.tag.save_tag_cross_by_task_id.DefaultSaveTagCrossByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.tag.save_tag_cross_by_task_id.SaveTagCrossByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.tag.update_tag_by_id.DefaultUpdateTagByIdUseCase
import com.example.inhabitnow.domain.use_case.tag.update_tag_by_id.UpdateTagByIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object TagDomainModule {

    @Provides
    fun provideReadTagsUseCase(
        tagRepository: TagRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): ReadTagsUseCase {
        return DefaultReadTagsUseCase(
            tagRepository = tagRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideReadTagIdsByTaskIdUseCase(
        tagRepository: TagRepository
    ): ReadTagIdsByTaskIdUseCase {
        return DefaultReadTagIdsByTaskIdUseCase(
            tagRepository = tagRepository
        )
    }

    @Provides
    fun provideSaveTagUseCase(
        tagRepository: TagRepository
    ): SaveTagUseCase {
        return DefaultSaveTagUseCase(
            tagRepository = tagRepository
        )
    }

    @Provides
    fun provideUpdateTagByIdUseCase(
        tagRepository: TagRepository
    ): UpdateTagByIdUseCase {
        return DefaultUpdateTagByIdUseCase(
            tagRepository = tagRepository
        )
    }

    @Provides
    fun provideDeleteTagByIdUseCase(
        tagRepository: TagRepository
    ): DeleteTagByIdUseCase {
        return DefaultDeleteTagByIdUseCase(
            tagRepository = tagRepository
        )
    }

    @Provides
    fun provideSaveTagCrossByTaskId(
        tagRepository: TagRepository
    ): SaveTagCrossByTaskIdUseCase {
        return DefaultSaveTagCrossByTaskIdUseCase(
            tagRepository = tagRepository
        )
    }

}