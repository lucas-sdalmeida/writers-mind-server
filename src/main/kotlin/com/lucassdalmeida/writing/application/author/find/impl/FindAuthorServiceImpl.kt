package com.lucassdalmeida.writing.application.author.find.impl

import com.lucassdalmeida.writing.application.author.find.FindAuthorService
import com.lucassdalmeida.writing.application.author.repository.AuthorRepository
import jakarta.persistence.EntityNotFoundException
import java.util.*

class FindAuthorServiceImpl(
    private val authorRepository: AuthorRepository,
) : FindAuthorService {
    override fun findByAccountId(accountId: UUID) = authorRepository.findByAccountId(accountId)
        ?: throw EntityNotFoundException("There's is no author for account $accountId")
}