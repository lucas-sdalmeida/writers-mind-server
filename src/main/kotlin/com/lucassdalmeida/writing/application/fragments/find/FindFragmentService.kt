package com.lucassdalmeida.writing.application.fragments.find

import com.lucassdalmeida.writing.application.fragments.repository.TimelinePositionDto
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

interface FindFragmentService {
    fun findById(id: UUID): ResponseModel

    data class ResponseModel(
        val id: UUID,
        val storyId: UUID,
        val narrativeThreadId: UUID?,
        val volumeId: UUID?,
        val characterId: UUID?,
        val title: String,
        val type: String,
        val summary: String?,
        val momentDate: LocalDate?,
        val momentTime: LocalTime?,
        val actualPosition: TimelinePositionDto,
        val width: Double?,
        val content: String?,
    )
}