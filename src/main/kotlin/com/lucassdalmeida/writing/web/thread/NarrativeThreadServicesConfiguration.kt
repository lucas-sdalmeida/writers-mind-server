package com.lucassdalmeida.writing.web.thread

import com.lucassdalmeida.writing.application.author.repository.AuthorRepository
import com.lucassdalmeida.writing.application.fragments.repository.StoryFragmentRepository
import com.lucassdalmeida.writing.application.story.repository.StoryRepository
import com.lucassdalmeida.writing.application.thread.create.CreateNarrativeThreadService
import com.lucassdalmeida.writing.application.thread.create.impl.CreateNarrativeThreadServiceImpl
import com.lucassdalmeida.writing.application.thread.repository.NarrativeThreadRepository
import com.lucassdalmeida.writing.application.volume.repository.VolumeRepository
import com.lucassdalmeida.writing.domain.service.UuidGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NarrativeThreadServicesConfiguration {
    @Bean
    fun createNarrativeThreadService(
        narrativeThreadRepository: NarrativeThreadRepository,
        storyRepository: StoryRepository,
        authorRepository: AuthorRepository,
        uuidGenerator: UuidGenerator,
        storyFragmentRepository: StoryFragmentRepository,
        volumeRepository: VolumeRepository,
    ): CreateNarrativeThreadService = CreateNarrativeThreadServiceImpl(
        narrativeThreadRepository,
        storyRepository,
        authorRepository,
        uuidGenerator,
        storyFragmentRepository,
        volumeRepository
    )
}