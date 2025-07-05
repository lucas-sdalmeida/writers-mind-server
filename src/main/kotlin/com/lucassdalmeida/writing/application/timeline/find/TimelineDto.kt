package com.lucassdalmeida.writing.application.timeline.find

import com.lucassdalmeida.writing.application.fragments.repository.TimelinePositionDto
import java.util.*

data class TimelineDto(val storyDto: StoryDto, val narrativeThreads: List<NarrativeThreadDto>)

data class StoryDto(val id: UUID, val title: String)

data class NarrativeThreadDto(
    val id: UUID?,
    val volumeId: UUID?,
    val characterId: UUID?,
    val title: String?,
    val lines: List<Line>,
)

data class Line(
    val index: Int,
    val preferences: LinePreferences,
    val points: List<Point>,
)

data class LinePreferences(val name: String, val color: String)

data class Point(
    val id: UUID,
    val narrativeThreadId: UUID?,
    val volumeId: UUID?,
    val characterId: UUID?,
    val chapterId: UUID?,
    val title: String,
    val type: String,
    val actualPosition: TimelinePositionDto,
    val width: Double?,
)
