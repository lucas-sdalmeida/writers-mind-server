package com.lucassdalmeida.writing.application.volume.repository

import com.lucassdalmeida.writing.domain.model.story.toStoryId
import com.lucassdalmeida.writing.domain.model.volume.Volume
import com.lucassdalmeida.writing.domain.model.volume.toVolumeId
import java.util.UUID

data class VolumeDto(val id: UUID, val storyId: UUID, val title: String, val summary: String?)

fun Volume.toDto() = VolumeDto(id.value, storyId.value, title, summary)

fun VolumeDto.toEntity() = Volume(id.toVolumeId(), storyId.toStoryId(), title, summary)
