package com.lucassdalmeida.writing.domain.model.timeline

import com.lucassdalmeida.writing.shared.Identifier
import java.util.UUID

@JvmInline
value class TimeLineId(val value: UUID) : Identifier {
    override fun toString() = value.toString()
}

fun UUID.toTimeLineId() = TimeLineId(this)
