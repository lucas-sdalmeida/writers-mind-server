package com.lucassdalmeida.writing.application.timeline.find

import java.util.UUID

interface FindTimelineService {
    fun findByStoryId(storyId: UUID): TimelineDto
}