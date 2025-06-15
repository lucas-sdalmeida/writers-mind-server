package com.lucassdalmeida.writing.application.author.repository

interface AuthorRepository {
    fun save(dto: AuthorDto)

    fun existsByPseudonym(pseudonym: String): Boolean
}