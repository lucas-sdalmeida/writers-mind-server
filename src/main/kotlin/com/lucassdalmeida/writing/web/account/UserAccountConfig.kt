package com.lucassdalmeida.writing.web.account

import com.lucassdalmeida.writing.application.account.login.LogInService
import com.lucassdalmeida.writing.application.account.login.impl.LogInServiceImpl
import com.lucassdalmeida.writing.application.account.repository.UserAccountRepository
import com.lucassdalmeida.writing.application.account.signup.SignUpService
import com.lucassdalmeida.writing.application.account.signup.impl.SignUpServiceImpl
import com.lucassdalmeida.writing.application.author.repository.AuthorRepository
import com.lucassdalmeida.writing.domain.service.UuidGenerator
import com.lucassdalmeida.writing.domain.service.password.PasswordEncoder
import com.lucassdalmeida.writing.infrastructure.account.PasswordEncoderImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class UserAccountConfig {
    @Bean
    fun passwordEncoder() = PasswordEncoderImpl(BCryptPasswordEncoder())

    @Bean
    fun signUpService(
        accountRepository: UserAccountRepository,
        authorRepository: AuthorRepository,
        passwordEncoder: PasswordEncoder,
        uuidGenerator: UuidGenerator,
    ): SignUpService = SignUpServiceImpl(accountRepository, authorRepository, uuidGenerator, passwordEncoder)

    @Bean
    fun logInService(
        accountRepository: UserAccountRepository,
        passwordEncoder: PasswordEncoder
    ): LogInService = LogInServiceImpl(accountRepository, passwordEncoder)
}