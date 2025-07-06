package com.lucassdalmeida.writing.web.fragment

import com.lucassdalmeida.writing.application.fragments.add.AddChapterService
import com.lucassdalmeida.writing.application.fragments.add.AddExcerptService
import com.lucassdalmeida.writing.application.fragments.find.FindFragmentService
import com.lucassdalmeida.writing.application.fragments.move.MoveFragmentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@RestController
@RequestMapping("/author/{authorId}/story/{storyId}/timeline")
@CrossOrigin
class StoryFragmentController(
    private val addExcerptService: AddExcerptService,
    private val addChapterService: AddChapterService,
    private val findFragmentService: FindFragmentService,
    private val moveFragmentService: MoveFragmentService,
) {
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

    @PostMapping("chapter/{chapterId}/fragment")
    fun postFragmentToChapter(
        @PathVariable authorId: UUID,
        @PathVariable storyId: UUID,
        @PathVariable chapterId: UUID,
        @RequestBody body: PostFragmentRequest,
    ): ResponseEntity<*> {
        val point = addExcerptService.addToChapter(chapterId, body.toAddExcerptRequest(authorId, storyId))
        return ResponseEntity.status(HttpStatus.CREATED).body(point)
    }

    @PostMapping("chapter")
    fun postChapter(
        @PathVariable authorId: UUID,
        @PathVariable storyId: UUID,
        @RequestBody body: PostChapterRequest,
    ): ResponseEntity<*> {
        val response = addChapterService.add(body.toAddChapterRequest(authorId, storyId))
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    private fun PostChapterRequest.toAddChapterRequest(
        authorId: UUID,
        storyId: UUID,
    ) = AddChapterService.RequestModel(
        storyId, authorId, narrativeThreadId,
        title, summary,
        momentDate, momentTime,
        line, x,
        excerptTitle, excerptSummary,
        File("./"),
    )

    @GetMapping("fragment/{fragmentId}")
    fun getFragment(
        @PathVariable authorId: UUID,
        @PathVariable storyId: UUID,
        @PathVariable fragmentId: UUID,
    ): ResponseEntity<*> {
        val response = findFragmentService.findById(fragmentId)
        return ResponseEntity.ok(response)
    }

    @PatchMapping("fragment/{fragmentId}/position")
    fun moveFragment(
        @PathVariable authorId: UUID,
        @PathVariable storyId: UUID,
        @PathVariable fragmentId: UUID,
        @RequestBody body: PatchFragmentRequest,
    ): ResponseEntity<*> {
        val (narrativeThreadId, line, deltaX) = body
        val response = moveFragmentService
            .move(MoveFragmentService.RequestModel(fragmentId, narrativeThreadId, line, deltaX))
        return ResponseEntity.ok(response)
    }

    @PatchMapping("fragment/{fragmentId}/position/chapter/{chapterId}")
    fun moveFragment(
        @PathVariable authorId: UUID,
        @PathVariable storyId: UUID,
        @PathVariable fragmentId: UUID,
        @PathVariable chapterId: UUID,
        @RequestBody body: PatchFragmentRequest,
    ): ResponseEntity<*> {
        val (narrativeThreadId, line, deltaX) = body
        val response = moveFragmentService
            .moveToChapter(chapterId, MoveFragmentService.RequestModel(fragmentId, narrativeThreadId, line, deltaX))
        return ResponseEntity.ok(response)
    }

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

    data class PostChapterRequest(
        val narrativeThreadId: UUID?,
        val title: String,
        val summary: String?,
        val momentDate: LocalDate?,
        val momentTime: LocalTime?,
        val line: Int,
        val x: Double,
        val excerptTitle: String,
        val excerptSummary: String?,
        val content: String?,
    )

    data class PatchFragmentRequest(
        val narrativeThreadId: UUID?,
        val line: Int,
        val deltaX: Double,
    )
}