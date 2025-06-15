package com.lucassdalmeida.writing.application.story.create

import java.util.UUID

interface CreateStoryService {
    fun create(request: RequestModel): UUID

    data class RequestModel(
        val title: String,
        val themes: String?,
        val objectives: String?,
        val mainPlot: String?,
        val genres: String?,
        val setting: String?,
        val summary: String?,
        val coverImageUri: String?,
        val authorId: UUID,
    )
}