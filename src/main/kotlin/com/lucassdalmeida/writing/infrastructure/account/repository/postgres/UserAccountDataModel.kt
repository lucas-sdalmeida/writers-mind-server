package com.lucassdalmeida.writing.infrastructure.account.repository.postgres

import com.lucassdalmeida.writing.application.account.repository.UserAccountDto
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "user_account")
data class UserAccountDataModel(
    @Id val id: UUID,
    val email: String,
    val password: String,
)

fun UserAccountDto.toDataModel() = UserAccountDataModel(id, email, password)

fun UserAccountDataModel.toDto() = UserAccountDto(id, email, password)
