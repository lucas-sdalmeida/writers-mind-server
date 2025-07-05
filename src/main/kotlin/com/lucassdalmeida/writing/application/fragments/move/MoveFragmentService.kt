package com.lucassdalmeida.writing.application.fragments.move

import com.lucassdalmeida.writing.application.fragments.repository.TimelinePositionDto
import java.util.UUID

interface MoveFragmentService {
    fun move(request: RequestModel): ResponseModel

    fun moveToChapter(chapterId: UUID, request: RequestModel): ResponseModel

    data class RequestModel(
        val id: UUID,
        val narrativeThreadId: UUID?,
        val line: Int,
        val x: Double,
    )

    data class ResponseModel(
        val fragment: FragmentDto,
        val chapter: FragmentDto?,
    )

    data class FragmentDto(
        val id: UUID,
        val storyId: UUID,
        val narrativeThreadId: UUID?,
        val volumeId: UUID?,
        val characterId: UUID?,
        val chapterId: UUID?,
        val title: String,
        val type: String,
        val actualPosition: TimelinePositionDto,
        val width: Double?,
    )
}