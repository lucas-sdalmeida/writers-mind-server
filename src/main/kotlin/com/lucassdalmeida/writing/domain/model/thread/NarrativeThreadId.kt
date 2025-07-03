package com.lucassdalmeida.writing.domain.model.thread

import com.lucassdalmeida.writing.shared.Identifier
import java.util.UUID

@JvmInline
value class NarrativeThreadId(val value: UUID) : Identifier

fun UUID.toNarrativeThreadId() = NarrativeThreadId(this)
