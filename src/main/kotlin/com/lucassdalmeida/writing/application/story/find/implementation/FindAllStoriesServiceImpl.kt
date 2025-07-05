package com.lucassdalmeida.writing.application.story.find.implementation

import com.lucassdalmeida.writing.application.story.find.FindAllStoriesService
import com.lucassdalmeida.writing.application.story.repository.StoryRepository
import java.util.UUID

class FindAllStoriesServiceImpl(private val repository: StoryRepository) : FindAllStoriesService {
    override fun findAllByAuthorId(authorId: UUID) = repository.findAll()
}