package com.lucassdalmeida.writing.infrastructure.story.repository.postgres

import com.lucassdalmeida.writing.application.story.repository.StoryDto
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "story")
data class StoryDataModel(
    @Id val id: UUID,
    val title: String,
    val themes: String?,
    val objectives: String?,
    val mainPlot: String?,
    val genres: String?,
    val setting: String?,
    val summary: String?,
    val coverImageUri: String?,
) {
    fun toDto() =
        StoryDto(id, title, themes, objectives, mainPlot, genres, setting, summary, coverImageUri)
}

fun StoryDto.toDataModel()
    = StoryDataModel(id, title, themes, objectives, mainPlot, genres, setting, summary, coverImageUri)
