package com.lucassdalmeida.writing.infrastructure.thread.repository.postgres

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface NarrativeThreadPostgresRepository : JpaRepository<NarrativeThreadDataModel, UUID> {
    fun findAllByStoryId(storyId: UUID): List<NarrativeThreadDataModel>

    fun existsByVolumeId(volumeId: UUID): Boolean

    fun existsByCharacterId(characterId: UUID): Boolean
}
