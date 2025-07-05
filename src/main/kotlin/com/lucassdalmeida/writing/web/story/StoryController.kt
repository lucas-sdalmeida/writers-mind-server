package com.lucassdalmeida.writing.web.story

import com.lucassdalmeida.writing.application.story.create.CreateStoryService
import com.lucassdalmeida.writing.application.story.create.CreateStoryService.RequestModel as CreateRequest
import com.lucassdalmeida.writing.application.story.find.FindAllStoriesService
import com.lucassdalmeida.writing.application.story.find.FindOneStoryService
import com.lucassdalmeida.writing.application.story.repository.StoryDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import kotlin.String

@RestController
@RequestMapping("/author/{authorId}/story")
@CrossOrigin
class StoryController(
    private val createStoryService: CreateStoryService,
    private val findOneStoryService: FindOneStoryService,
    private val findAllStoryService: FindAllStoriesService,
) {
    @PostMapping
    fun postStory(@PathVariable authorId: UUID, @RequestBody request: PostRequest): ResponseEntity<*> {
        val id = createStoryService.create(request.toCreateRequest(authorId))
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(PostResponse(id))
    }
    
    private fun PostRequest.toCreateRequest(authorId: UUID) = CreateRequest(
        title,
        themes, objectives, mainPlot, genres, setting,
        summary,
        coverImageUri,
        authorId,
    )

    @GetMapping("{storyId}")
    fun getOneStory(@PathVariable authorId: UUID, @PathVariable storyId: UUID): ResponseEntity<*> {
        val story = findOneStoryService.findById(storyId, authorId)
        return ResponseEntity.ok(story)
    }

    @GetMapping
    fun getAllStories(@PathVariable authorId: UUID): ResponseEntity<*> {
        val stories = findAllStoryService.findAllByAuthorId(authorId)
        return ResponseEntity.ok(GetAllResponse(stories))
    }

    data class PostRequest(
        val title: String,
        val themes: String?,
        val objectives: String?,
        val mainPlot: String?,
        val genres: String?,
        val setting: String?,
        val summary: String?,
        val coverImageUri: String?,
    )
    
    private data class PostResponse(val storyId: UUID)

    private data class GetAllResponse(val stories: List<StoryDto>)
}