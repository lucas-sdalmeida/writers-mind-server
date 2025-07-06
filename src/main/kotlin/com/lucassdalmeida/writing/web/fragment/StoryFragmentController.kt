package com.lucassdalmeida.writing.web.fragment

import com.lucassdalmeida.writing.application.fragments.add.AddExcerptService
import org.apache.coyote.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@RestController
@RequestMapping("/author/{authorId}/story/{storyId}/timeline")
@CrossOrigin
class StoryFragmentController(private val addExcerptService: AddExcerptService) {
    @PostMapping("fragment")
    fun postFragment(
        @PathVariable authorId: UUID,
        @PathVariable storyId: UUID,
        @RequestBody body: PostFragmentRequest,
    ): ResponseEntity<*> {
        val newPoint = addExcerptService.add(body.toAddExcerptRequest(authorId, storyId))
        return ResponseEntity.status(HttpStatus.CREATED).body(newPoint)
    }

    private fun PostFragmentRequest.toAddExcerptRequest(
        authorId: UUID,
        storyId: UUID,
    ) = AddExcerptService.RequestModel(
        storyId, authorId, narrativeThreadId,
        title, summary,
        momentDate, momentTime,
        line, x,
        File("./"),
    )

    data class PostFragmentRequest(
        val narrativeThreadId: UUID?,
        val title: String,
        val summary: String?,
        val momentDate: LocalDate?,
        val momentTime: LocalTime?,
        val line: Int,
        val x: Double,
        val content: String?,
    )
}