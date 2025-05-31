package com.lucassdalmeida.writing.infrastructure.story.repository.postgres

import com.lucassdalmeida.writing.application.story.repository.StoryDto
import com.lucassdalmeida.writing.application.story.repository.StoryRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Repository
class StoryRepositoryImpl(private val postgresRepository: StoryPostgresRepository) : StoryRepository {
    override fun save(story: StoryDto) {
        postgresRepository.save(story.toDataModel())
    }

    override fun findById(id: UUID) = postgresRepository
        .findById(id)
        .getOrNull()
        ?.toDto()

    override fun findAll() = postgresRepository
        .findAll()
        .map { it.toDto() }
}
