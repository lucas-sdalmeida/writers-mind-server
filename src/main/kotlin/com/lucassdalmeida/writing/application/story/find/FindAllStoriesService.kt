package com.lucassdalmeida.writing.application.story.find

import com.lucassdalmeida.writing.application.story.repository.StoryDto

interface FindAllStoriesService {
    fun findAll(): List<StoryDto>
}