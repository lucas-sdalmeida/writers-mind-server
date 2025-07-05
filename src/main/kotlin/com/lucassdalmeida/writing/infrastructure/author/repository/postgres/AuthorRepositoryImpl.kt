package com.lucassdalmeida.writing.infrastructure.author.repository.postgres

import com.lucassdalmeida.writing.application.author.repository.AuthorDto
import com.lucassdalmeida.writing.application.author.repository.AuthorRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Repository
class AuthorRepositoryImpl(
    private val innerRepository: AuthorPostgresRepository,
) : AuthorRepository {
    override fun save(dto: AuthorDto) {
        innerRepository.save(dto.toDataModel())
    }

    override fun findByAccountId(accountId: UUID) = innerRepository
        .findByAccountId(accountId)
        .getOrNull()
        ?.toDto()

    override fun existsById(id: UUID) = innerRepository.existsById(id)

    override fun existsByPseudonym(pseudonym: String) = innerRepository.existsByPseudonym(pseudonym)
}