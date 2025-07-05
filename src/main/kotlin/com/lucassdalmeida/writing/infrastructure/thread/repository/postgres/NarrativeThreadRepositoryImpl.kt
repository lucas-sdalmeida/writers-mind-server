package com.lucassdalmeida.writing.infrastructure.thread.repository.postgres

import com.lucassdalmeida.writing.application.thread.repository.NarrativeThreadDto
import com.lucassdalmeida.writing.application.thread.repository.NarrativeThreadRepository
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

class NarrativeThreadRepositoryImpl(
    private val innerRepository: NarrativeThreadPostgresRepository,
): NarrativeThreadRepository {
    override fun save(dto: NarrativeThreadDto) {
        innerRepository.save(dto.toDataModel())
    }

    override fun findById(id: UUID) = innerRepository
        .findById(id)
        .getOrNull()
        ?.toDto()

    override fun findAllByStoryId(storyId: UUID) = innerRepository
        .findAllByStoryId(storyId)
        .map { it.toDto() }

    override fun existsByVolumeId(volumeId: UUID) = innerRepository.existsByVolumeId(volumeId)

    override fun existsByCharacterId(characterId: UUID) = innerRepository.existsByCharacterId(characterId)
}