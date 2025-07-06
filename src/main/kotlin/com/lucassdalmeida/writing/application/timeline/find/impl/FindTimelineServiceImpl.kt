package com.lucassdalmeida.writing.application.timeline.find.impl

import com.lucassdalmeida.writing.application.fragments.repository.StoryFragmentDto
import com.lucassdalmeida.writing.application.fragments.repository.StoryFragmentRepository
import com.lucassdalmeida.writing.application.fragments.repository.TimelinePositionDto
import com.lucassdalmeida.writing.application.fragments.repository.toEntity
import com.lucassdalmeida.writing.application.shared.exceptions.EntityNotFoundException
import com.lucassdalmeida.writing.application.story.repository.StoryRepository
import com.lucassdalmeida.writing.application.story.repository.toStory
import com.lucassdalmeida.writing.application.thread.repository.NarrativeThreadRepository
import com.lucassdalmeida.writing.application.thread.repository.toEntity
import com.lucassdalmeida.writing.application.timeline.find.*
import com.lucassdalmeida.writing.application.volume.repository.VolumeRepository
import com.lucassdalmeida.writing.application.volume.repository.toEntity
import com.lucassdalmeida.writing.domain.model.fragment.Chapter
import com.lucassdalmeida.writing.domain.model.fragment.Excerpt
import com.lucassdalmeida.writing.domain.model.fragment.StoryFragment
import com.lucassdalmeida.writing.domain.model.story.Story
import com.lucassdalmeida.writing.domain.model.thread.CharacterBiographyThread
import com.lucassdalmeida.writing.domain.model.thread.NarrativeThread
import com.lucassdalmeida.writing.domain.model.thread.NarrativeThreadId
import com.lucassdalmeida.writing.domain.model.thread.VolumeThread
import java.util.*

class FindTimelineServiceImpl(
    private val storyRepository: StoryRepository,
    private val narrativeThreadRepository: NarrativeThreadRepository,
    private val storyFragmentRepository: StoryFragmentRepository,
    private val volumeRepository: VolumeRepository,
) : FindTimelineService {
    override fun findByStoryId(storyId: UUID): TimelineDto {
        val story = storyRepository.findById(storyId)
            ?.toStory()
            ?: throw EntityNotFoundException("There is no story with id $storyId")
        val threads = narrativeThreadRepository.findAllByStoryId(storyId)
            .map { it.toEntity() }

        val unboundedPoints = storyFragmentRepository
            .findAllByNarrativeThreadId(null)
            .map { it.toEntity() }
            .map { it.toPointDto(null) }
            .groupBy { it.actualPosition.line }
            .map { (line, points) -> Line(line, getLinePreferences(line, null), points) }
            .sortedBy { it.index }
        val unboundedPointsThread = NarrativeThreadDto(
                null, null, null, null,
            unboundedPoints.ifEmpty { listOf(getDefaultLine()) }
            )

        val narrativeThreadsDtos = threads.map { it.toTimelineThreadDto() }

        return TimelineDto(
            story.id.value,
            story.toTimelineStoryDto(),
            mutableListOf<NarrativeThreadDto>().also {
                it.add(unboundedPointsThread)
                it.addAll(narrativeThreadsDtos)
            }.sortedBy { it.title }
        )
    }

    private fun StoryFragment.toPointDto(thread: NarrativeThread?) = Point(
        id.value,
        narrativeThreadId?.value,
        if (thread is VolumeThread) thread.volumeId.value else null,
        if (thread is CharacterBiographyThread) thread.characterId.value else null,
        if (this is Excerpt) chapterId?.value else null,
        title,
        if (this is Excerpt) "excerpt" else "chapter",
        TimelinePositionDto(actualPosition.line, actualPosition.x),
        if (this is Chapter) lastPosition.x - actualPosition.x else null,
    )

    private fun getLinePreferences(line: Int, thread: NarrativeThread?): LinePreferences {
        val hexDigits = "0123456789abcdef"

        val color = when {
            thread == null && line == 0 -> "#10c3e2"
            else -> buildString {
                append('#')
                while (length < 7) append(hexDigits.random())
            }
        }

        return LinePreferences("linha ${line + 1}", color)
    }

    private fun getDefaultLine() =
        Line(0, getLinePreferences(0, null), listOf())

    private fun NarrativeThread.toTimelineThreadDto(): NarrativeThreadDto {
        val points = storyFragmentRepository
            .findAllByNarrativeThreadId(id.value)
            .map { it.toEntity() }
            .map { it.toPointDto(this) }

        val lines = points
            .groupBy { it.actualPosition.line }
            .map { (line, points) ->
                Line(line, getLinePreferences(line, this), points)
            }.sortedBy { it.index }

        val volume = if (this is VolumeThread) volumeRepository.findById(volumeId.value)?.toEntity() else null

        return NarrativeThreadDto(
            id.value,
            if (this is VolumeThread) volumeId.value else null,
            if (this is CharacterBiographyThread) characterId.value else null,
            volume?.title,
            lines
        )
    }

    private fun Story.toTimelineStoryDto() = StoryDto(id.value, title)
}