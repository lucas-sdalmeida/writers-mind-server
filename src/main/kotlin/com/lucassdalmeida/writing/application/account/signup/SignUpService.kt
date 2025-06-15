package com.lucassdalmeida.writing.application.account.signup

import java.util.UUID

interface SignUpService {
    fun signUp(request: RequestModel): UUID

    data class RequestModel(
        val name: String,
        val pseudonym: String?,
        val email: String,
        val plainPassword: String,
    )
}