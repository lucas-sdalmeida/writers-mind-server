package com.lucassdalmeida.writing.infrastructure.author.repository.postgres

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AuthorPostgresRepository : JpaRepository<AuthorDataModel, UUID> {
    fun existsByPseudonym(pseudonym: String): Boolean
}
