package com.lucassdalmeida.writing.domain.model.character

import com.lucassdalmeida.writing.shared.Identifier
import java.util.UUID

@JvmInline
value class CharacterId(val value: UUID) : Identifier {
    override fun toString() = value.toString()
}

fun UUID.toCharacterId() = CharacterId(this)
