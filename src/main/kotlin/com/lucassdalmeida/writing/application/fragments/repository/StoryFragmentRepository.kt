package com.lucassdalmeida.writing.application.fragments.repository

import java.util.UUID

interface StoryFragmentRepository {
    fun save(dto: StoryFragmentDto)

    fun findAllByChapter(chapterId: UUID): List<StoryFragmentDto>

    fun findAllByPackId(packId: UUID?): List<StoryFragmentDto>
}