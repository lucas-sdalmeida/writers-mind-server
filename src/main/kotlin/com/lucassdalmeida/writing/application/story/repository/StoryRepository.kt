package com.lucassdalmeida.writing.application.story.repository

import java.util.UUID

interface StoryRepository {
    fun save(story: StoryDto)

    fun findById(id: UUID): StoryDto?

    fun findAll(): List<StoryDto>

    fun findAllByAuthorId(authorId: UUID): List<StoryDto>
}