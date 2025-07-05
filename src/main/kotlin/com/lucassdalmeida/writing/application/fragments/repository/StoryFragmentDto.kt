package com.lucassdalmeida.writing.application.fragments.repository

import com.lucassdalmeida.writing.domain.model.author.toAuthorId
import com.lucassdalmeida.writing.domain.model.fragment.Chapter
import com.lucassdalmeida.writing.domain.model.fragment.Excerpt
import com.lucassdalmeida.writing.domain.model.fragment.StoryFragment
import com.lucassdalmeida.writing.domain.model.fragment.TimeLinePosition
import com.lucassdalmeida.writing.domain.model.fragment.toStoryFragmentId
import com.lucassdalmeida.writing.domain.model.story.toStoryId
import com.lucassdalmeida.writing.domain.model.thread.toNarrativeThreadId
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
    val chapterId: UUID?,
    val chapterLastPositionLine: Int?,
    val chapterLastPositionX: Double?,
)

fun StoryFragment.toDto() = StoryFragmentDto(
    id.value,
    storyId.value, authorId.value, narrativeThreadId?.value,
    title, summary,
    momentDate, momentTime,
    placementPosition.line, placementPosition.x,
    actualPosition.line, actualPosition.x,
    if (this is Excerpt) fileUri else null,
    if (this is Excerpt) chapterId?.value else null,
    if (this is Chapter) lastPosition.line else null,
    if(this is Chapter) lastPosition.x else null,
)

fun StoryFragmentDto.toEntity() = when {
    fileUri != null -> Excerpt(
        id.toStoryFragmentId(), storyId.toStoryId(), authorId.toAuthorId(),
        narrativeThreadId?.toNarrativeThreadId(),
        title, summary,
        momentDate, momentTime,
        TimeLinePosition(placementPositionLine, placementPositionX),
        fileUri,
        TimeLinePosition(actualPositionLine, actualPositionX),
        chapterId?.toStoryFragmentId(),
    )
    else -> Chapter(
        id.toStoryFragmentId(), storyId.toStoryId(), authorId.toAuthorId(),
        narrativeThreadId?.toNarrativeThreadId(),
        title, summary,
        momentDate, momentTime,
        TimeLinePosition(placementPositionLine, placementPositionX),
        TimeLinePosition(actualPositionLine, actualPositionX),
        TimeLinePosition(chapterLastPositionLine!!, chapterLastPositionX!!),
    )
}
