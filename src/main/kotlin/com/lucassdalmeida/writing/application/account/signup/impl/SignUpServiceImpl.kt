package com.lucassdalmeida.writing.application.account.signup.impl

import com.lucassdalmeida.writing.application.account.repository.UserAccountRepository
import com.lucassdalmeida.writing.application.account.repository.toDto
import com.lucassdalmeida.writing.application.account.signup.SignUpService
import com.lucassdalmeida.writing.application.account.signup.SignUpService.RequestModel
import com.lucassdalmeida.writing.application.author.repository.AuthorRepository
import com.lucassdalmeida.writing.application.author.repository.toDto
import com.lucassdalmeida.writing.application.shared.exceptions.EntityAlreadyExistsException
import com.lucassdalmeida.writing.domain.model.account.UserAccount
import com.lucassdalmeida.writing.domain.model.account.toUserAccountId
import com.lucassdalmeida.writing.domain.model.author.Author
import com.lucassdalmeida.writing.domain.model.author.toAuthorId
import com.lucassdalmeida.writing.domain.service.UuidGenerator
import com.lucassdalmeida.writing.domain.service.password.PasswordEncoder
import java.util.UUID

class SignUpServiceImpl(
    private val accountRepository: UserAccountRepository,
    private val authorRepository: AuthorRepository,
    private val uuidGenerator: UuidGenerator,
    private val passwordEncoder: PasswordEncoder,
) : SignUpService {
    override fun signUp(request: RequestModel): UUID {
        if (accountRepository.existsByEmail(request.email))
            throw EntityAlreadyExistsException("Unable to create account! Email: ${request.email} already exists!")
        if (request.pseudonym != null && authorRepository.existsByPseudonym(request.pseudonym))
            throw EntityAlreadyExistsException("The pseudonym ${request.pseudonym} is already in use!")

        val id = uuidGenerator.randomUuid()
        val password = passwordEncoder.encode(request.plainPassword)

        val account = UserAccount(id.toUserAccountId(), request.email, password)
        val author = Author(id.toAuthorId(), request.name, request.pseudonym, account.id)

        accountRepository.save(account.toDto())
        authorRepository.save(author.toDto())

        return id
    }
}