package com.lucassdalmeida.writing.application.timeline.repository

import com.lucassdalmeida.writing.domain.model.timeline.TimeLine
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
