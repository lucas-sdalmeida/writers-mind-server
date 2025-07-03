package com.lucassdalmeida.writing.infrastructure.fragment.repository.postgres

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface StoryFragmentPostgresRepository : JpaRepository<StoryFragmentDataModel, UUID> {
    fun findAllByNarrativeThreadId(narrativeThreadId: UUID?): List<StoryFragmentDataModel>
}
