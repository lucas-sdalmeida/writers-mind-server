package com.lucassdalmeida.writing.application.fragments.add

import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

interface AddExcerptService {
    fun add(request: RequestModel): ResponseModel

    data class RequestModel(
        val storyId: UUID,
        val authorId: UUID,
        val title: String,
        val summary: String?,
        val momentDate: LocalDate?,
        val momentTime: LocalTime?,
        val narrativeThreadId: UUID?,
        val line: Int,
        val x: Double,
        val content: File,
    )

    data class ResponseModel(
        val storyId: UUID,
        val narrativeThreadId: UUID?,
        val volumeId: UUID?,
        val characterId: UUID?,
        val chapterId: UUID?,
        val title: String,
        val actualPosition: TimelinePositionDto
    )

    data class TimelinePositionDto(val line: Int, val x: Double)
}