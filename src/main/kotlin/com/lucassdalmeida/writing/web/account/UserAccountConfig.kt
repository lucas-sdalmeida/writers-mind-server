package com.lucassdalmeida.writing.web.account

import com.lucassdalmeida.writing.infrastructure.account.PasswordEncoderImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class UserAccountConfig {
    @Bean
    fun passwordEncoder() = PasswordEncoderImpl(BCryptPasswordEncoder())
}