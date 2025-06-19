package com.lucassdalmeida.writing.domain.model.pack

import com.lucassdalmeida.writing.shared.Identifier
import java.util.UUID

@JvmInline
value class StoryPackId(val value: UUID?) : Identifier

fun UUID?.toStoryPackId() = StoryPackId(this)
