package com.lucassdalmeida.writing.domain.model.account

import com.lucassdalmeida.writing.shared.Entity

class UserAccount(
    id: UserAccountId,
    val email: String,
    val password: String,
) : Entity<UserAccountId>(id)
