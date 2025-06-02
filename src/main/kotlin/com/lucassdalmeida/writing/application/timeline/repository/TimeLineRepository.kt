package com.lucassdalmeida.writing.application.timeline.repository

interface TimeLineRepository {
    fun save(dto: TimeLineDto)
}