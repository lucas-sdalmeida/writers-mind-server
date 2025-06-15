package com.lucassdalmeida.writing.domain.service.password

interface PasswordEncrypter {
    fun encrypt(password: String): String

    fun check(plain: String, encrypted: String)
}
