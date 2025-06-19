package com.lucassdalmeida.writing.application.story.repository

import com.lucassdalmeida.writing.domain.model.author.toAuthorId
import com.lucassdalmeida.writing.domain.model.story.Story
import com.lucassdalmeida.writing.domain.model.story.toStoryId
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
    val authorId: UUID,
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
    authorId.value,
)

fun StoryDto.toStory() = Story(
    id.toStoryId(),
    title,
    objectives,
    themes,
    genres,
    mainPlot,
    setting,
    summary,
    coverImageUri,
    authorId.toAuthorId(),
)
