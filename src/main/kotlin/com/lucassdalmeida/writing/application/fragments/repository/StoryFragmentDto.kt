package com.lucassdalmeida.writing.application.fragments.repository

import com.lucassdalmeida.writing.domain.model.fragment.Chapter
import com.lucassdalmeida.writing.domain.model.fragment.Excerpt
import com.lucassdalmeida.writing.domain.model.fragment.StoryFragment
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

data class StoryFragmentDto(
    val id: UUID,
    val storyId: UUID,
    val authorId: UUID,
    val narrativeThreadId: UUID?,
    val title: String,
    val summary: String?,
    val momentDate: LocalDate?,
    val momentTime: LocalTime?,
    val placementPositionLine: Int,
    val placementPositionX: Double,
    val actualPositionLine: Int,
    val actualPositionX: Double,
    val fileUri: String?,
    val excerpts: List<UUID>?,
)

fun StoryFragment.toDto() = StoryFragmentDto(
    id.value,
    storyId.value, authorId.value, narrativeThreadId.value,
    title, summary,
    momentDate, momentTime,
    placementPosition.line, placementPosition.x,
    actualPosition.line, actualPosition.x,
    if (this is Excerpt) fileUri else null,
    if (this is Chapter) excerpts.map { it.value } else null,
)
