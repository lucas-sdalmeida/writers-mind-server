package com.lucassdalmeida.writing.infrastructure.account.repository.postgres

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface UserAccountPostgresRepository : JpaRepository<UserAccountDataModel, UUID> {
    fun findByEmail(email: String): Optional<UserAccountDataModel>

    fun existsByEmail(email: String): Boolean
}
