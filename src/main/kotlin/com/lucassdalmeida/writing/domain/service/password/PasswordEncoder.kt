package com.lucassdalmeida.writing.domain.service.password

interface PasswordEncoder {
    fun encode(password: String): String

    fun check(plain: String, encrypted: String)
}
