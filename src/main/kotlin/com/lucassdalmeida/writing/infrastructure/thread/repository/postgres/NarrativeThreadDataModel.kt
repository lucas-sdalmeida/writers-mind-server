package com.lucassdalmeida.writing.infrastructure.thread.repository.postgres

import com.lucassdalmeida.writing.application.thread.repository.NarrativeThreadDto
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "narrative_thread")
data class NarrativeThreadDataModel(
    @Id val id: UUID,
    val storyId: UUID,
    val volumeId: UUID?,
    val characterId: UUID?,
)

fun NarrativeThreadDto.toDataModel() = NarrativeThreadDataModel(id, storyId, volumeId, characterId)

fun NarrativeThreadDataModel.toDto() = NarrativeThreadDto(id, storyId, volumeId, characterId)
