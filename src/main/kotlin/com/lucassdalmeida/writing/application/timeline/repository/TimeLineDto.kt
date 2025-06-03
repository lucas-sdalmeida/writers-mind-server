package com.lucassdalmeida.writing.application.timeline.repository

import com.lucassdalmeida.writing.domain.model.fragment.toExcerptId
import com.lucassdalmeida.writing.domain.model.timeline.TimeLine
import com.lucassdalmeida.writing.domain.model.timeline.TimeLine.TimePoint
import com.lucassdalmeida.writing.domain.model.timeline.toTimeLineId
import java.time.LocalDateTime
import java.util.UUID

data class TimeLineDto(val id: UUID, val lines: List<List<TimePointDto>>) {
    data class TimePointDto(val fragmentId: UUID, val title: String, val pointX: Double, val moment: LocalDateTime?)
}

fun TimeLine.toDto() = TimeLineDto(
    id.value,
    lines.map { line ->
        line.map {
            (fragmentId, title, pointX, moment) ->
                TimeLineDto.TimePointDto(fragmentId.value, title, pointX, moment)
        }
    },
)

fun TimeLineDto.toTimeLine(): TimeLine {
    val timeLine = TimeLine(id.toTimeLineId())
    lines
        .map { line -> line.map { it.toTimePoint() } }
        .forEach { it.forEach(timeLine::addPoint) }
    return timeLine
}

private fun TimeLineDto.TimePointDto.toTimePoint() =
    TimePoint(fragmentId.toExcerptId(), title, pointX, moment)
