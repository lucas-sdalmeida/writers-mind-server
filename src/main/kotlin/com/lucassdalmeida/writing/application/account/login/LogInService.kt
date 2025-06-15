package com.lucassdalmeida.writing.application.account.login

import java.util.UUID

interface LogInService {
    fun logIn(request: RequestModel): UUID

    data class RequestModel(val email: String, val password: String)
}