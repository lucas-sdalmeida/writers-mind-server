package com.lucassdalmeida.writing.application.story.repository

interface StoryRepository {
    fun save(story: StoryDto)
}