package com.lucassdalmeida.writing.infrastructure.author.repository.postgres

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface AuthorPostgresRepository : JpaRepository<AuthorDataModel, UUID> {
    fun findByAccountId(accountId: UUID): Optional<AuthorDataModel>

    fun existsByPseudonym(pseudonym: String): Boolean
}
