package com.lucassdalmeida.writing.web.thread

import com.lucassdalmeida.writing.application.thread.create.CreateNarrativeThreadService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/author/{authorId}/story/{storyId}/timeline/thread")
@CrossOrigin
class NarrativeThreadController(
    private val createNarrativeThreadService: CreateNarrativeThreadService,
) {
    @PostMapping("/volume")
    fun postThread(
        @PathVariable authorId: UUID,
        @PathVariable storyId: UUID,
        @RequestBody body: PostRequest,
    ): ResponseEntity<*> {
        val request = CreateNarrativeThreadService
            .RequestModel(authorId, storyId, body.title, body.lines, "volume")
        val thread = createNarrativeThreadService.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(thread)
    }

    data class PostRequest(val title: String, val lines: List<Int>)
}