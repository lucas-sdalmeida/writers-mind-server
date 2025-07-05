package com.lucassdalmeida.writing.application.story.find.implementation

import com.lucassdalmeida.writing.application.author.repository.AuthorRepository
import com.lucassdalmeida.writing.application.author.repository.toAuthor
import com.lucassdalmeida.writing.application.shared.exceptions.EntityNotFoundException
import com.lucassdalmeida.writing.application.shared.exceptions.UnauthenticatedUserException
import com.lucassdalmeida.writing.application.shared.exceptions.UnauthorizedUserException
import com.lucassdalmeida.writing.application.story.find.FindOneStoryService
import com.lucassdalmeida.writing.application.story.repository.StoryDto
import com.lucassdalmeida.writing.application.story.repository.StoryRepository
import com.lucassdalmeida.writing.application.story.repository.toDto
import com.lucassdalmeida.writing.application.story.repository.toStory
import java.util.*

class FindOneStoryServiceImpl(
    private val repository: StoryRepository,
    private val authorRepository: AuthorRepository,
) : FindOneStoryService {
    override fun findById(id: UUID, authorId: UUID): StoryDto {
        val author = authorRepository.findById(authorId)
            ?.toAuthor()
            ?: throw UnauthenticatedUserException("There is not an author with id $authorId")
        val story = repository.findById(id)
            ?.toStory()
            ?: throw EntityNotFoundException("There is not a story with id $id")
        if (story.authorId != author.id)
            throw UnauthorizedUserException("The story $id does not belongs to author $authorId")
        return story.toDto()
    }
}