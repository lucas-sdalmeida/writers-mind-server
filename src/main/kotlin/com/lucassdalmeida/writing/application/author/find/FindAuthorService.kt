package com.lucassdalmeida.writing.application.author.find

import com.lucassdalmeida.writing.application.author.repository.AuthorDto
import java.util.UUID

interface FindAuthorService {
    fun findByAccountId(accountId: UUID): AuthorDto
}