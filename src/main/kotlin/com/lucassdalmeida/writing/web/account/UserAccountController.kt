package com.lucassdalmeida.writing.web.account

import com.lucassdalmeida.writing.application.account.login.LogInService
import com.lucassdalmeida.writing.application.account.signup.SignUpService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/account")
@CrossOrigin
class UserAccountController(
    private val logInService: LogInService,
    private val signUpService: SignUpService,
) {
    @PostMapping("/signup")
    fun signUp(@RequestBody body: SignUpRequest): ResponseEntity<*> {
        val (name, pseudonym, email, password) = body
        val id = signUpService.signUp(SignUpService.RequestModel(name, pseudonym, email, password))
        return ResponseEntity.status(HttpStatus.CREATED).body(SignUpAndLoginResponse(id))
    }

    data class SignUpRequest(
        val name: String,
        val pseudonym: String?,
        val email: String,
        val password: String,
    )

    data class SignUpAndLoginResponse(val accountId: UUID)

    @PostMapping("/login")
    fun logIn(@RequestBody body: LogInService.RequestModel): ResponseEntity<*> {
        val id = logInService.logIn(body)
        return ResponseEntity.ok(SignUpAndLoginResponse(id))
    }
}