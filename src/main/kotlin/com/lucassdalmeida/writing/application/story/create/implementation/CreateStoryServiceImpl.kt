package com.lucassdalmeida.writing.application.story.create.implementation

import com.lucassdalmeida.writing.application.author.repository.AuthorRepository
import com.lucassdalmeida.writing.application.shared.exceptions.EntityNotFoundException
import com.lucassdalmeida.writing.application.story.create.CreateStoryService
import com.lucassdalmeida.writing.application.story.create.CreateStoryService.RequestModel
import com.lucassdalmeida.writing.application.story.repository.StoryRepository
import com.lucassdalmeida.writing.application.story.repository.toDto
import com.lucassdalmeida.writing.domain.model.author.toAuthorId
import com.lucassdalmeida.writing.domain.model.story.Story
import com.lucassdalmeida.writing.domain.model.story.toStoryId
import com.lucassdalmeida.writing.domain.service.UuidGenerator
import java.util.UUID

class CreateStoryServiceImpl(
    private val repository: StoryRepository,
    private val authorRepository: AuthorRepository,
    private val uuidGenerator: UuidGenerator,
) : CreateStoryService {
    override fun create(request: RequestModel): UUID {
        if (!authorRepository.existsById(request.authorId))
            throw EntityNotFoundException("Unable to create story because the author ${request.authorId} doe not exists!")

        val id = uuidGenerator.randomUuid()
        val story = request.toStory(id)

        repository.save(story.toDto())
        return id
    }

    private fun RequestModel.toStory(id: UUID) = Story(
        id.toStoryId(),
        title,
        objectives,
        themes,
        genres,
        mainPlot,
        setting,
        summary,
        coverImageUri,
        authorId.toAuthorId(),
    )
}