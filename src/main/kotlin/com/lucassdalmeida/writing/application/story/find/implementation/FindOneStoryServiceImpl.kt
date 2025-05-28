package com.lucassdalmeida.writing.application.story.find.implementation

import com.lucassdalmeida.writing.application.story.find.FindOneStoryService
import com.lucassdalmeida.writing.application.story.repository.StoryRepository
import java.util.*

class FindOneStoryServiceImpl(private val repository: StoryRepository) : FindOneStoryService {
    override fun findById(id: UUID) = repository.findById(id)
}