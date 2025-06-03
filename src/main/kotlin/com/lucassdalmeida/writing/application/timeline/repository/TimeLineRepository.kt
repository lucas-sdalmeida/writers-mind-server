package com.lucassdalmeida.writing.application.timeline.repository

import java.util.UUID

interface TimeLineRepository {
    fun save(dto: TimeLineDto)

    fun findById(id: UUID): TimeLineDto?
}