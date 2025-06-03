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

@RestController
@RequestMapping("/story")
@CrossOrigin
class StoryController(
    private val createStoryService: CreateStoryService,
    private val findOneStoryService: FindOneStoryService,
    private val findAllStoryService: FindAllStoriesService,
) {
    @PostMapping
    fun postStory(@RequestBody request: CreateRequest): ResponseEntity<*> {
        val id = createStoryService.create(request)
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(PostResponse(id))
    }

    @GetMapping("{storyId}")
    fun getOneStory(@PathVariable storyId: UUID): ResponseEntity<*> {
        val story = findOneStoryService.findById(storyId)
        if (story == null) return ResponseEntity.notFound().build<Any>()
        return ResponseEntity.ok(story)
    }

    @GetMapping
    fun getAllStories(): ResponseEntity<*> {
        val stories = findAllStoryService.findAll()
        return ResponseEntity.ok(GetAllResponse(stories))
    }

    private data class PostResponse(val storyId: UUID)

    private data class GetAllResponse(val stories: List<StoryDto>)
}