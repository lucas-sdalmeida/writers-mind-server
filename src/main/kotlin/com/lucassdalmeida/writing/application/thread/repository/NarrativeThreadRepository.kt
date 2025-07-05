package com.lucassdalmeida.writing.application.thread.repository

import java.util.*

interface NarrativeThreadRepository {
    fun save(dto: NarrativeThreadDto)

    fun findById(id: UUID): NarrativeThreadDto?

    fun findAllByStoryId(storyId: UUID): List<NarrativeThreadDto>

    fun existsByVolumeId(volumeId: UUID): Boolean

    fun existsByCharacterId(characterId: UUID): Boolean
}