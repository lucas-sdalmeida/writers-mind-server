package com.lucassdalmeida.writing.application.story.create.implementation

import com.lucassdalmeida.writing.application.story.create.CreateStoryService
import com.lucassdalmeida.writing.application.story.create.CreateStoryService.RequestModel
import com.lucassdalmeida.writing.application.story.repository.StoryRepository
import com.lucassdalmeida.writing.application.story.repository.toDto
import com.lucassdalmeida.writing.application.timeline.repository.TimeLineRepository
import com.lucassdalmeida.writing.application.timeline.repository.toDto
import com.lucassdalmeida.writing.domain.model.story.Story
import com.lucassdalmeida.writing.domain.model.story.toStoryId
import com.lucassdalmeida.writing.domain.model.timeline.TimeLine
import com.lucassdalmeida.writing.domain.model.timeline.toTimeLineId
import com.lucassdalmeida.writing.domain.service.UuidGenerator
import java.util.UUID

class CreateStoryServiceImpl(
    private val repository: StoryRepository,
    private val timeLineRepository: TimeLineRepository,
    private val uuidGenerator: UuidGenerator,
) : CreateStoryService {
    override fun create(request: RequestModel): UUID {
        val id = uuidGenerator.randomUuid()
        val story = request.toStory(id)
        val timeLine = TimeLine(id.toTimeLineId())

        repository.save(story.toDto())
        timeLineRepository.save(timeLine.toDto())

        return id
    }

    private fun RequestModel.toStory(id: UUID) = Story(
        id.toStoryId(),
        title,
        objectives,
        themes,
        genres,
        mainPlot,
        setting,
        summary,
        coverImageUri
    )
}