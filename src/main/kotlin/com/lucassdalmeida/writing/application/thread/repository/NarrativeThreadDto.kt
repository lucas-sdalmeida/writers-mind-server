package com.lucassdalmeida.writing.application.thread.repository

import com.lucassdalmeida.writing.domain.model.character.toCharacterId
import com.lucassdalmeida.writing.domain.model.story.toStoryId
import com.lucassdalmeida.writing.domain.model.thread.CharacterBiographyThread
import com.lucassdalmeida.writing.domain.model.thread.NarrativeThread
import com.lucassdalmeida.writing.domain.model.thread.VolumeThread
import com.lucassdalmeida.writing.domain.model.thread.toNarrativeThreadId
import com.lucassdalmeida.writing.domain.model.volume.toVolumeId
import java.util.UUID

data class NarrativeThreadDto(
    val id: UUID,
    val storyId: UUID,
    val volumeId: UUID?,
    val characterId: UUID?,
)

fun NarrativeThread.toDto() = NarrativeThreadDto(
    id.value, storyId.value,
    if (this is VolumeThread) volumeId.value else null,
    if (this is CharacterBiographyThread) characterId.value else null,
)

fun NarrativeThreadDto.toEntity() = when {
    volumeId != null ->
        VolumeThread(
            id.toNarrativeThreadId(),
            storyId.toStoryId(),
            volumeId.toVolumeId()
        )
    else ->
        CharacterBiographyThread(
            id.toNarrativeThreadId(),
            storyId.toStoryId(),
            characterId!!.toCharacterId()
        )
}
