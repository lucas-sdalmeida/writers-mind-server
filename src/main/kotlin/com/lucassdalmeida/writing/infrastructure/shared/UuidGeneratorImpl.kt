package com.lucassdalmeida.writing.infrastructure.shared

import com.lucassdalmeida.writing.domain.service.UuidGenerator
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UuidGeneratorImpl : UuidGenerator {
    override fun randomUuid() = UUID.randomUUID()!!
}