package com.lucassdalmeida.writing.infrastructure.account
import org.springframework.security.crypto.password.PasswordEncoder as SpringEncoder
import com.lucassdalmeida.writing.domain.service.password.PasswordEncoder

class PasswordEncoderImpl(private val innerEncoder: SpringEncoder) : PasswordEncoder {
    override fun encode(password: String): String {
        return innerEncoder.encode(password)
    }

    override fun check(plain: String, encrypted: String) {
        innerEncoder.matches(plain, encrypted)
    }
}