package com.lucassdalmeida.writing.infrastructure.volume.repository.postgres

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface VolumePostgresRepository : JpaRepository<VolumeDataModel, UUID>
