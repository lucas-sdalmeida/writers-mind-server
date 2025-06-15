package com.lucassdalmeida.writing.application.account.repository

import com.lucassdalmeida.writing.domain.model.account.UserAccount
import com.lucassdalmeida.writing.domain.model.account.toUserAccountId
import java.util.UUID

data class UserAccountDto(
    val id: UUID,
    val email: String,
    val password: String,
)

fun UserAccount.toDto() = UserAccountDto(id.value, email, password)

fun UserAccountDto.toUserAccount() = UserAccount(id.toUserAccountId(), email, password)
