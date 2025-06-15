package com.lucassdalmeida.writing.application.account.login.impl

import com.lucassdalmeida.writing.application.account.login.LogInService
import com.lucassdalmeida.writing.application.account.login.LogInService.RequestModel
import com.lucassdalmeida.writing.application.account.repository.UserAccountRepository
import com.lucassdalmeida.writing.application.account.repository.toUserAccount
import com.lucassdalmeida.writing.application.shared.exceptions.EntityNotFoundException
import com.lucassdalmeida.writing.domain.service.password.PasswordEncoder
import java.util.UUID

class LogInServiceImpl(
    private val userAccountRepository: UserAccountRepository,
    private val passwordEncoder: PasswordEncoder,
) : LogInService {
    override fun logIn(request: RequestModel): UUID {
        val account = userAccountRepository
            .findByEmail(request.email)
            ?.toUserAccount()
            ?: throw EntityNotFoundException("There's no user with such email!")
        passwordEncoder.check(request.password, account.password)
        return account.id.value
    }
}