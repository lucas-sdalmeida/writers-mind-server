package com.lucassdalmeida.writing.infrastructure.volume.repository.postgres

import com.lucassdalmeida.writing.application.volume.repository.VolumeDto
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "volume")
data class VolumeDataModel(@Id val id: UUID, val storyId: UUID, val title: String, val summary: String?)

fun VolumeDto.toDataModel() = VolumeDataModel(id, storyId, title, summary)

fun VolumeDataModel.toDto() = VolumeDto(id, storyId, title, summary)
