package com.lucassdalmeida.writing.infrastructure.author.repository.postgres

import com.lucassdalmeida.writing.application.author.repository.AuthorDto
import com.lucassdalmeida.writing.infrastructure.account.repository.postgres.UserAccountDataModel
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "author")
data class AuthorDataModel(
    @Id val id: UUID,
    val name: String,
    val pseudonym: String?,
    @OneToOne(targetEntity = UserAccountDataModel::class) val accountId: UUID,
)

fun AuthorDto.toDataModel() = AuthorDataModel(id, name, pseudonym, accountId)

fun AuthorDataModel.toDto() = AuthorDto(id, name, pseudonym, accountId)
