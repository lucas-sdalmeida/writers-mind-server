package com.lucassdalmeida.writing.domain.service

import java.util.UUID

interface UuidGenerator {
    fun randomUuid(): UUID
}
