package com.lucassdalmeida.writing.application.account.repository

interface UserAccountRepository {
    fun save(dto: UserAccountDto)

    fun findByEmail(email: String): UserAccountDto?

    fun existsByEmail(email: String): Boolean
}