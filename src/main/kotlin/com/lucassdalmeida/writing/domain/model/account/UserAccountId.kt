package com.lucassdalmeida.writing.domain.model.account

import com.lucassdalmeida.writing.shared.Identifier
import java.util.UUID

@JvmInline
value class UserAccountId(val value: UUID) : Identifier

fun UUID.toUserAccountId() = UserAccountId(this)
