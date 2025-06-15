package com.lucassdalmeida.writing.infrastructure.account.repository.postgres

import com.lucassdalmeida.writing.application.account.repository.UserAccountDto
import com.lucassdalmeida.writing.application.account.repository.UserAccountRepository
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class UserAccountRepositoryImpl(
    private val innerRepository: UserAccountPostgresRepository,
) : UserAccountRepository {
    override fun save(dto: UserAccountDto) {
        innerRepository.save(dto.toDataModel())
    }

    override fun findByEmail(email: String) = innerRepository
        .findByEmail(email)
        .getOrNull()
        ?.toDto()

    override fun existsByEmail(email: String) = innerRepository.existsByEmail(email)
}