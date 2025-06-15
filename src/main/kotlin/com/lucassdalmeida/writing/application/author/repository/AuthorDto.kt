package com.lucassdalmeida.writing.application.author.repository

import com.lucassdalmeida.writing.domain.model.account.toUserAccountId
import com.lucassdalmeida.writing.domain.model.author.Author
import com.lucassdalmeida.writing.domain.model.author.toAuthorId
import java.util.UUID

data class AuthorDto(val id: UUID, val name: String, val pseudonym: String?, val accountId: UUID)

fun Author.toDto() = AuthorDto(id.value, name, pseudonym, accountId.value)

fun AuthorDto.toAuthor() = Author(id.toAuthorId(), name, pseudonym, accountId.toUserAccountId())
