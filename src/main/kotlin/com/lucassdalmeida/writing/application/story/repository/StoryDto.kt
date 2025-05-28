package com.lucassdalmeida.writing.application.story.repository

import com.lucassdalmeida.writing.domain.model.story.Story
import java.util.UUID

data class StoryDto(
    val id: UUID,
    val title: String,
    val themes: String?,
    val objectives: String?,
    val mainPlot: String?,
    val genres: String?,
    val setting: String?,
    val summary: String?,
    val coverImageUri: String?,
)

fun Story.toDto() = StoryDto(
    id.value,
    title,
    themes,
    objectives,
    mainPlot,
    genres,
    setting,
    summary,
    coverImageUri,
)
