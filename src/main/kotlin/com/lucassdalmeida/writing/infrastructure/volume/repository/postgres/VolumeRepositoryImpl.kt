package com.lucassdalmeida.writing.infrastructure.volume.repository.postgres

import com.lucassdalmeida.writing.application.volume.repository.VolumeDto
import com.lucassdalmeida.writing.application.volume.repository.VolumeRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Repository
class VolumeRepositoryImpl(private val innerRepository: VolumePostgresRepository) : VolumeRepository {
    override fun save(dto: VolumeDto) {
        innerRepository.save(dto.toDataModel())
    }

    override fun findById(id: UUID) = innerRepository
        .findById(id)
        .getOrNull()
        ?.toDto()
}
