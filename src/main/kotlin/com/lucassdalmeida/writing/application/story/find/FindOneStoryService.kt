package com.lucassdalmeida.writing.application.story.find

import com.lucassdalmeida.writing.application.story.repository.StoryDto
import java.util.UUID

interface FindOneStoryService {
    fun findById(id: UUID, authorId: UUID): StoryDto
}