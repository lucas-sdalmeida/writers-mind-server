package com.lucassdalmeida.writing.application.fragments.repository

import java.util.UUID

interface StoryFragmentRepository {
    fun save(dto: StoryFragmentDto)

    fun findById(id: UUID): StoryFragmentDto?

    fun findAllByNarrativeThreadId(threadId: UUID?): List<StoryFragmentDto>
}