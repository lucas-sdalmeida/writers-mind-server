package com.lucassdalmeida.writing.domain.model.fragment

import com.lucassdalmeida.writing.shared.Identifier
import java.util.UUID

@JvmInline
value class ExcerptId(val value: UUID) : Identifier {
    override fun toString() = value.toString()
}

fun UUID.toExcerptId() = ExcerptId(this)
