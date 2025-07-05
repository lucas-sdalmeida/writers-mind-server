package com.lucassdalmeida.writing.application.author.repository

import java.util.UUID

interface AuthorRepository {
    fun save(dto: AuthorDto)

    fun findById(id: UUID): AuthorDto?

    fun findByAccountId(accountId: UUID): AuthorDto?

    fun existsById(id: UUID): Boolean

    fun existsByPseudonym(pseudonym: String): Boolean
}