package com.lucassdalmeida.writing.application.thread.create.impl

import com.lucassdalmeida.writing.application.author.repository.AuthorRepository
import com.lucassdalmeida.writing.application.author.repository.toAuthor
import com.lucassdalmeida.writing.application.fragments.repository.StoryFragmentRepository
import com.lucassdalmeida.writing.application.fragments.repository.TimelinePositionDto
import com.lucassdalmeida.writing.application.fragments.repository.toDto
import com.lucassdalmeida.writing.application.fragments.repository.toEntity
import com.lucassdalmeida.writing.application.shared.exceptions.EntityNotFoundException
import com.lucassdalmeida.writing.application.shared.exceptions.UnauthenticatedUserException
import com.lucassdalmeida.writing.application.shared.exceptions.UnauthorizedUserException
import com.lucassdalmeida.writing.application.story.repository.StoryRepository
import com.lucassdalmeida.writing.application.story.repository.toStory
import com.lucassdalmeida.writing.application.thread.create.CreateNarrativeThreadService
import com.lucassdalmeida.writing.application.thread.create.CreateNarrativeThreadService.RequestModel
import com.lucassdalmeida.writing.application.thread.create.CreateNarrativeThreadService.ResponseModel
import com.lucassdalmeida.writing.application.thread.repository.NarrativeThreadRepository
import com.lucassdalmeida.writing.application.thread.repository.toDto
import com.lucassdalmeida.writing.application.timeline.find.Line
import com.lucassdalmeida.writing.application.timeline.find.LinePreferences
import com.lucassdalmeida.writing.application.timeline.find.NarrativeThreadDto
import com.lucassdalmeida.writing.application.timeline.find.Point
import com.lucassdalmeida.writing.application.volume.repository.VolumeRepository
import com.lucassdalmeida.writing.application.volume.repository.toDto
import com.lucassdalmeida.writing.domain.model.fragment.Chapter
import com.lucassdalmeida.writing.domain.model.fragment.Excerpt
import com.lucassdalmeida.writing.domain.model.fragment.StoryFragment
import com.lucassdalmeida.writing.domain.model.story.toStoryId
import com.lucassdalmeida.writing.domain.model.thread.CharacterBiographyThread
import com.lucassdalmeida.writing.domain.model.thread.NarrativeThread
import com.lucassdalmeida.writing.domain.model.thread.VolumeThread
import com.lucassdalmeida.writing.domain.model.thread.toNarrativeThreadId
import com.lucassdalmeida.writing.domain.model.volume.Volume
import com.lucassdalmeida.writing.domain.model.volume.toVolumeId
import com.lucassdalmeida.writing.domain.service.UuidGenerator

class CreateNarrativeThreadServiceImpl(
    private val narrativeThreadRepository: NarrativeThreadRepository,
    private val storyRepository: StoryRepository,
    private val authorRepository: AuthorRepository,
    private val uuidGenerator: UuidGenerator,
    private val storyFragmentRepository: StoryFragmentRepository,
    private val volumeRepository: VolumeRepository,
) : CreateNarrativeThreadService {
    override fun create(request: RequestModel): ResponseModel {
        checkPreconditions(request)

        val id = uuidGenerator.randomUuid()
        val volume =
            Volume(id.toVolumeId(), request.storyId.toStoryId(), request.title, "")
        val volumeThread =
            VolumeThread(id.toNarrativeThreadId(), request.storyId.toStoryId(), id.toVolumeId())

        val fragments = storyFragmentRepository
            .findAllByNarrativeThreadId(null)
            .map { it.toEntity() }
            .filter { it.actualPosition.line in request.lines }
            .groupBy { it.actualPosition.line }
            .toSortedMap()
            .values
            .mapIndexed { index, points ->
                points.map {
                    it.apply {
                        narrativeThreadId = id.toNarrativeThreadId()
                        placementPosition = placementPosition.copy(line = index)
                    }
                }
            }

        narrativeThreadRepository.save(volumeThread.toDto())
        volumeRepository.save(volume.toDto())

        fragments
            .flatten()
            .forEach { storyFragmentRepository.save(it.toDto()) }

        return ResponseModel(
            NarrativeThreadDto(
                id, id, null,
                request.title,
                fragments
                    .mapIndexed { index, points ->
                        Line(
                            index,
                            getLinePreferences(index, volumeThread),
                            points.map {
                                it.toPointDto(volumeThread)
                            }
                        )
                    }
            )
        )
    }

    private fun checkPreconditions(request: RequestModel) {
        val author = authorRepository.findById(request.authorId)
            ?.toAuthor()
            ?: throw UnauthenticatedUserException("There is not an author with id ${request.authorId}")
        val story = storyRepository.findById(request.storyId)
            ?.toStory()
            ?: throw EntityNotFoundException("The story with id ${request.storyId} does not exist!")
        if (story.authorId != author.id)
            throw UnauthorizedUserException("The author ${request.authorId} have no access to story ${request.storyId}")
    }

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

    private fun StoryFragment.toPointDto(thread: NarrativeThread?) = Point(
        id.value,
        storyId.value,
        narrativeThreadId?.value,
        if (thread is VolumeThread) thread.volumeId.value else null,
        if (thread is CharacterBiographyThread) thread.characterId.value else null,
        if (this is Excerpt) chapterId?.value else null,
        title,
        if (this is Excerpt) "excerpt" else "chapter",
        TimelinePositionDto(actualPosition.line, actualPosition.x),
        if (this is Chapter) lastPosition.x - actualPosition.x else null,
    )
}