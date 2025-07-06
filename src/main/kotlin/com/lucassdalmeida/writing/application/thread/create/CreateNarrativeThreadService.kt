package com.lucassdalmeida.writing.application.thread.create

import java.util.UUID
import com.lucassdalmeida.writing.application.timeline.find.NarrativeThreadDto as TimelineThreadDto

interface CreateNarrativeThreadService {
    fun create(request: RequestModel): ResponseModel

    data class RequestModel(
        val authorId: UUID,
        val storyId: UUID,
        val title: String,
        val lines: List<Int>,
        val type: String,
    )

    data class ResponseModel(val narrativeThread: TimelineThreadDto)
}