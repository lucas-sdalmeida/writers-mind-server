package com.lucassdalmeida.writing.application.fragments.add

import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

interface AddExcerptService {
    fun add(request: RequestModel): UUID

    data class RequestModel(
        val storyId: UUID,
        val authorId: UUID,
        val title: String,
        val summary: String?,
        val momentDate: LocalDate?,
        val momentTime: LocalTime?,
        val packId: UUID?,
        val line: Int,
        val x: Double,
        val content: File,
    )
}