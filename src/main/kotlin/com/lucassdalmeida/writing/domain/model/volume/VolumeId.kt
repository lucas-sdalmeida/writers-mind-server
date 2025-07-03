package com.lucassdalmeida.writing.domain.model.volume

import com.lucassdalmeida.writing.shared.Identifier
import java.util.UUID

@JvmInline
value class VolumeId(val value: UUID) : Identifier
