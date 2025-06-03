package com.lucassdalmeida.writing.application.timeline.add

import java.time.LocalDateTime
import java.util.UUID

interface AddPointService {
    fun addPoint(request: RequestModel)

    data class RequestModel(
        val timeLineId: UUID,
        val title: String,
        val fileUri: String,
        val summary: String?,
        val pointX: Double,
        val moment: LocalDateTime?,
    )
}