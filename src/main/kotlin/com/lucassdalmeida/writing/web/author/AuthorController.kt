package com.lucassdalmeida.writing.web.author

import com.lucassdalmeida.writing.application.author.find.FindAuthorService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/author")
class AuthorController(private val findAuthorService: FindAuthorService) {
    @GetMapping("/account/{accountId}")
    fun getAuthor(@PathVariable accountId: UUID): ResponseEntity<*> {
        val author = findAuthorService.findByAccountId(accountId)
        return ResponseEntity.ok(author)
    }
}