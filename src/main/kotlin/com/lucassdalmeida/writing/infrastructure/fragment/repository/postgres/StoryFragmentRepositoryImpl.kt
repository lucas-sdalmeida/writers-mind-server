package com.lucassdalmeida.writing.infrastructure.fragment.repository.postgres

import com.lucassdalmeida.writing.application.fragments.repository.StoryFragmentDto
import com.lucassdalmeida.writing.application.fragments.repository.StoryFragmentRepository
import java.util.UUID

class StoryFragmentRepositoryImpl(
    private val innerRepository: StoryFragmentPostgresRepository,
) : StoryFragmentRepository {
    override fun save(dto: StoryFragmentDto) {
        innerRepository.save(dto.toDataModel())
    }

    override fun findAllByNarrativeThreadId(threadId: UUID?) = innerRepository
        .findAllByNarrativeThreadId(threadId)
        .map { it.toDto() }
}