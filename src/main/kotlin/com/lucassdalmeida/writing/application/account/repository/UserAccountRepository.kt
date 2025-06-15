package com.lucassdalmeida.writing.application.account.repository

interface UserAccountRepository {
    fun save(dto: UserAccountDto)

    fun existsByEmail(email: String): Boolean
}