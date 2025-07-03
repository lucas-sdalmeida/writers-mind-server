package com.lucassdalmeida.writing.application.volume.repository

import java.util.UUID

interface VolumeRepository {
    fun save(dto: VolumeDto)

    fun findById(id: UUID): VolumeDto?
}