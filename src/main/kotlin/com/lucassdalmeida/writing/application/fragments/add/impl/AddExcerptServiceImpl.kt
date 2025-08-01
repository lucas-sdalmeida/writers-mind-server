package com.lucassdalmeida.writing.application.fragments.add.impl

import com.lucassdalmeida.writing.application.author.repository.AuthorRepository
import com.lucassdalmeida.writing.application.fragments.add.AddExcerptService
import com.lucassdalmeida.writing.application.fragments.add.AddExcerptService.RequestModel
import com.lucassdalmeida.writing.application.fragments.add.AddExcerptService.ResponseModel
import com.lucassdalmeida.writing.application.fragments.file.SaveFileService
import com.lucassdalmeida.writing.application.fragments.repository.StoryFragmentRepository
import com.lucassdalmeida.writing.application.fragments.repository.TimelinePositionDto
import com.lucassdalmeida.writing.application.fragments.repository.toDto
import com.lucassdalmeida.writing.application.fragments.repository.toEntity
import com.lucassdalmeida.writing.application.shared.exceptions.EntityNotFoundException
import com.lucassdalmeida.writing.application.shared.exceptions.UnauthenticatedUserException
import com.lucassdalmeida.writing.application.shared.exceptions.UnauthorizedUserException
import com.lucassdalmeida.writing.application.story.repository.StoryRepository
import com.lucassdalmeida.writing.application.story.repository.toStory
import com.lucassdalmeida.writing.application.thread.repository.NarrativeThreadRepository
import com.lucassdalmeida.writing.application.thread.repository.toEntity
import com.lucassdalmeida.writing.domain.model.author.toAuthorId
import com.lucassdalmeida.writing.domain.model.fragment.Chapter
import com.lucassdalmeida.writing.domain.model.fragment.Excerpt
import com.lucassdalmeida.writing.domain.model.fragment.StoryFragment
import com.lucassdalmeida.writing.domain.model.fragment.TimeLinePosition
import com.lucassdalmeida.writing.domain.model.fragment.toStoryFragmentId
import com.lucassdalmeida.writing.domain.model.story.toStoryId
import com.lucassdalmeida.writing.domain.model.thread.CharacterBiographyThread
import com.lucassdalmeida.writing.domain.model.thread.NarrativeThread
import com.lucassdalmeida.writing.domain.model.thread.VolumeThread
import com.lucassdalmeida.writing.domain.model.thread.toNarrativeThreadId
import com.lucassdalmeida.writing.domain.service.UuidGenerator
import java.util.*

class AddExcerptServiceImpl(
    private val storyFragmentRepository: StoryFragmentRepository,
    private val storyRepository: StoryRepository,
    private val authorRepository: AuthorRepository,
    private val narrativeThreadRepository: NarrativeThreadRepository,
    private val saveFileService: SaveFileService,
    private val uuidGenerator: UuidGenerator,
) : AddExcerptService {
    override fun add(request: RequestModel): ResponseModel {
        val (storyId, authorId, narrativeThreadId) = request
        checkPreconditions(storyId, authorId)
        val narrativeThread = fetchNarrativeThread(narrativeThreadId)

        val id = uuidGenerator.randomUuid()
        val excerpt = request.toExcerpt(id)
        calculateLine(excerpt, request)

        storyFragmentRepository.save(excerpt.toDto())
        saveFileService.save(request.content)

        return ResponseModel(
            id,
            storyId,
            narrativeThreadId,
            if (narrativeThread is VolumeThread) narrativeThread.volumeId.value else null,
            if (narrativeThread is CharacterBiographyThread) narrativeThread.characterId.value else null,
            null,
            excerpt.title,
            "excerpt",
            TimelinePositionDto(excerpt.actualPosition.line, excerpt.actualPosition.x),
        )
    }

    private fun checkPreconditions(storyId: UUID, authorId: UUID) {
        if (!authorRepository.existsById(authorId))
            throw UnauthenticatedUserException("Unable to create the excerpt because user $authorId does not exist!")
        val story = storyRepository.findById(storyId)
            ?.toStory()
            ?: throw EntityNotFoundException("The story of id $storyId does not exists!")
        if (story.authorId != authorId.toAuthorId())
            throw UnauthorizedUserException("The user $authorId does not have access to story $storyId!")
    }

    private fun fetchNarrativeThread(narrativeThreadId: UUID?): NarrativeThread? {
        if (narrativeThreadId == null) return null
        return narrativeThreadRepository
            .findById(narrativeThreadId)
            ?.toEntity()
            ?: throw EntityNotFoundException("The narrative thread of id $narrativeThreadId does not exist!")
    }

    private fun RequestModel.toExcerpt(id: UUID) = Excerpt(
        id.toStoryFragmentId(), storyId.toStoryId(), authorId.toAuthorId(),
        narrativeThreadId?.toNarrativeThreadId(),
        title, summary,
        momentDate, momentTime,
        TimeLinePosition(line, x),
        content.absolutePath,
    )

    private fun calculateLine(excerpt: StoryFragment, request: RequestModel) {
        val lines = storyFragmentRepository.findAllByNarrativeThreadId(request.narrativeThreadId)
            .map { it.toEntity() }
            .groupBy { it.actualPosition.line }
        if (lines.isEmpty()) {
            excerpt.apply { placementPosition = placementPosition.copy(line = 0) }
            return
        }
        if (lines[excerpt.actualPosition.line] == null)
            return
        if (lines[excerpt.actualPosition.line]?.none { areColliding(it, excerpt) } == true) {
            return
        }

        for ((line, points) in lines) {
            if (line == excerpt.actualPosition.line) continue

            excerpt.apply { placementPosition = placementPosition.copy(line = line) }
            if (points.any { areColliding(it, excerpt) }) continue

            return
        }

        val line = lines.size + 1
        excerpt.apply { placementPosition = placementPosition.copy(line = line) }
    }

    private fun areColliding(fragment: StoryFragment, other: StoryFragment): Boolean {
        if (fragment == other)
            return false
        if (fragment is Chapter && other is Excerpt && fragment.id == other.chapterId)
            return false
        if (fragment is Excerpt && other is Chapter && fragment.chapterId == other.id)
            return false
        return fragment.isNear(other)
    }

    override fun addToChapter(chapterId: UUID, request: RequestModel): ResponseModel {
        val (storyId, authorId, narrativeThreadId) = request
        checkPreconditions(storyId, authorId)

        val narrativeThread = fetchNarrativeThread(narrativeThreadId)
        val chapter = fetchChapter(chapterId)

        val id = uuidGenerator.randomUuid()
        val excerpt = request.toExcerpt(id)

        excerpt.chapterId = chapter.id
        chapter.addExcerpt(excerpt)

        calculateLine(chapter, request)

        storyFragmentRepository.save(excerpt.toDto())
        storyFragmentRepository.save(chapter.toDto())
        saveFileService.save(request.content)

        return ResponseModel(
            id,
            storyId,
            narrativeThreadId,
            if (narrativeThread is VolumeThread) narrativeThread.volumeId.value else null,
            if (narrativeThread is CharacterBiographyThread) narrativeThread.characterId.value else null,
            chapterId,
            excerpt.title,
            "excerpt",
            TimelinePositionDto(excerpt.actualPosition.line, excerpt.actualPosition.x),
        )
    }

    private fun fetchChapter(chapterId: UUID): Chapter {
        val fragment = storyFragmentRepository.findById(chapterId)
            ?.toEntity()
            ?: throw EntityNotFoundException("There is not chapter with id $chapterId")
        require(fragment is Chapter) { "Fragment of id $chapterId is not a chapter" }
        return fragment
    }
}