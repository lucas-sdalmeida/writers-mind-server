package com.lucassdalmeida.writing.domain.model.author

import com.lucassdalmeida.writing.shared.Identifier
import java.util.UUID

@JvmInline
value class AuthorId(val value: UUID) : Identifier {
    override fun toString() = value.toString()
}

fun UUID.toAuthorId() = AuthorId(this)
