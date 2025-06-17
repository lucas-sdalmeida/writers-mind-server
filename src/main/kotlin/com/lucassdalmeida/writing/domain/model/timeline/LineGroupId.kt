package com.lucassdalmeida.writing.domain.model.timeline

import com.lucassdalmeida.writing.shared.Identifier

@JvmInline
value class LineGroupId(val value: Int) : Identifier

fun Int.toLineGroupId() = LineGroupId(this)
