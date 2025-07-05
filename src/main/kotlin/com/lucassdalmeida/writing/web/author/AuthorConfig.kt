package com.lucassdalmeida.writing.web.author

import com.lucassdalmeida.writing.application.author.find.FindAuthorService
import com.lucassdalmeida.writing.application.author.find.impl.FindAuthorServiceImpl
import com.lucassdalmeida.writing.application.author.repository.AuthorRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthorConfig {
    @Bean
    fun findAuthorService(authorRepository: AuthorRepository): FindAuthorService =
        FindAuthorServiceImpl(authorRepository)
}