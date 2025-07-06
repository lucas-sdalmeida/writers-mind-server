package com.lucassdalmeida.writing.infrastructure.fragment.repository.postgres

import com.lucassdalmeida.writing.application.fragments.repository.StoryFragmentDto
import com.lucassdalmeida.writing.application.fragments.repository.StoryFragmentRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Repository
class StoryFragmentRepositoryImpl(
    private val innerRepository: StoryFragmentPostgresRepository,
) : StoryFragmentRepository {
    override fun save(dto: StoryFragmentDto) {
        innerRepository.save(dto.toDataModel())
    }

    override fun findById(id: UUID) = innerRepository
        .findById(id)
        .getOrNull()
        ?.toDto()

    override fun findAllByNarrativeThreadId(threadId: UUID?) = innerRepository
        .findAllByNarrativeThreadId(threadId)
        .map { it.toDto() }
}