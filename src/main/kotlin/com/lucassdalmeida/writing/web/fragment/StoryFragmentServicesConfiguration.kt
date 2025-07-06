package com.lucassdalmeida.writing.web.fragment

import com.lucassdalmeida.writing.application.author.repository.AuthorRepository
import com.lucassdalmeida.writing.application.fragments.add.AddChapterService
import com.lucassdalmeida.writing.application.fragments.add.AddExcerptService
import com.lucassdalmeida.writing.application.fragments.add.impl.AddChapterServiceImpl
import com.lucassdalmeida.writing.application.fragments.add.impl.AddExcerptServiceImpl
import com.lucassdalmeida.writing.application.fragments.file.SaveFileService
import com.lucassdalmeida.writing.application.fragments.find.FindFragmentService
import com.lucassdalmeida.writing.application.fragments.find.impl.FindFragmentServiceImpl
import com.lucassdalmeida.writing.application.fragments.repository.StoryFragmentRepository
import com.lucassdalmeida.writing.application.story.repository.StoryRepository
import com.lucassdalmeida.writing.application.thread.repository.NarrativeThreadRepository
import com.lucassdalmeida.writing.domain.service.UuidGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StoryFragmentServicesConfiguration {
    @Bean
    fun addExcerptService(
        storyFragmentRepository: StoryFragmentRepository,
        storyRepository: StoryRepository,
        authorRepository: AuthorRepository,
        narrativeThreadRepository: NarrativeThreadRepository,
        saveFileService: SaveFileService,
        uuidGenerator: UuidGenerator,
    ): AddExcerptService = AddExcerptServiceImpl(
        storyFragmentRepository,
        storyRepository,
        authorRepository,
        narrativeThreadRepository,
        saveFileService,
        uuidGenerator,
    )

    @Bean
    fun addChapterService(
        storyFragmentRepository: StoryFragmentRepository,
        storyRepository: StoryRepository,
        authorRepository: AuthorRepository,
        narrativeThreadRepository: NarrativeThreadRepository,
        saveFileService: SaveFileService,
        uuidGenerator: UuidGenerator
    ): AddChapterService = AddChapterServiceImpl(
        storyFragmentRepository,
        storyRepository,
        authorRepository,
        narrativeThreadRepository,
        saveFileService,
        uuidGenerator,
    )

    @Bean
    fun findFragmentService(
        storyFragmentRepository: StoryFragmentRepository,
        narrativeThreadRepository: NarrativeThreadRepository,
    ): FindFragmentService = FindFragmentServiceImpl(storyFragmentRepository, narrativeThreadRepository)
}