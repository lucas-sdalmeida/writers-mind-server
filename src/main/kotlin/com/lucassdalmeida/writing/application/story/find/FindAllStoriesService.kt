package com.lucassdalmeida.writing.application.story.find

import com.lucassdalmeida.writing.application.story.repository.StoryDto
import java.util.UUID

interface FindAllStoriesService {
    fun findAllByAuthorId(authorId: UUID): List<StoryDto>
}