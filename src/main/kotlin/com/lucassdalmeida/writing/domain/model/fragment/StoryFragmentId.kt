package com.lucassdalmeida.writing.domain.model.fragment

import com.lucassdalmeida.writing.shared.Identifier
import java.util.UUID

@JvmInline
value class StoryFragmentId(val value: UUID) : Identifier

fun UUID.toStoryFragmentId() = StoryFragmentId(this)
