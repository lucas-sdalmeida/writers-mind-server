package com.lucassdalmeida.writing.domain.model.story

import com.lucassdalmeida.writing.shared.Identifier
import java.util.UUID

@JvmInline
value class StoryId(val value: UUID) : Identifier {
    override fun toString() = value.toString()
}

fun UUID.toStoryId() = StoryId(this)
