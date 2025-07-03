package com.lucassdalmeida.writing.infrastructure.volume.repository.postgres

import com.lucassdalmeida.writing.application.volume.repository.VolumeDto
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.springframework.data.annotation.Id
import java.util.UUID

@Entity
@Table(name = "volume")
data class VolumeDataModel(@Id val id: UUID, val storyId: UUID, val title: String, val summary: String?)

fun VolumeDto.toDataModel() = VolumeDataModel(id, storyId, title, summary)

fun VolumeDataModel.toDto() = VolumeDto(id, storyId, title, summary)
