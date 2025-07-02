package com.lucassdalmeida.writing.web.story

import com.lucassdalmeida.writing.application.author.repository.AuthorRepository
import com.lucassdalmeida.writing.application.story.create.CreateStoryService
import com.lucassdalmeida.writing.application.story.create.implementation.CreateStoryServiceImpl
import com.lucassdalmeida.writing.application.story.find.FindAllStoriesService
import com.lucassdalmeida.writing.application.story.find.FindOneStoryService
import com.lucassdalmeida.writing.application.story.find.implementation.FindAllStoriesServiceImpl
import com.lucassdalmeida.writing.application.story.find.implementation.FindOneStoryServiceImpl
import com.lucassdalmeida.writing.application.story.repository.StoryRepository
import com.lucassdalmeida.writing.domain.service.UuidGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StoryServicesConfig {
    @Bean
    fun createStoryService(
        repository: StoryRepository,
        authorRepository: AuthorRepository,
        uuidGenerator: UuidGenerator,
    ): CreateStoryService =
        CreateStoryServiceImpl(repository, authorRepository, uuidGenerator)

    @Bean
    fun findOneStoryService(repository: StoryRepository): FindOneStoryService =
        FindOneStoryServiceImpl(repository)

    @Bean
    fun findAllStoryService(repository: StoryRepository): FindAllStoriesService =
        FindAllStoriesServiceImpl(repository)
}