package com.lucassdalmeida.writing.application.fragments.add.impl

import com.lucassdalmeida.writing.application.author.repository.AuthorRepository
import com.lucassdalmeida.writing.application.fragments.add.AddChapterService
import com.lucassdalmeida.writing.application.fragments.add.AddChapterService.ChapterDto
import com.lucassdalmeida.writing.application.fragments.add.AddChapterService.ExcerptDto
import com.lucassdalmeida.writing.application.fragments.add.AddChapterService.RequestModel
import com.lucassdalmeida.writing.application.fragments.add.AddChapterService.ResponseModel
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
import java.util.UUID

class AddChapterServiceImpl(
    private val storyFragmentRepository: StoryFragmentRepository,
    private val storyRepository: StoryRepository,
    private val authorRepository: AuthorRepository,
    private val narrativeThreadRepository: NarrativeThreadRepository,
    private val saveFileService: SaveFileService,
    private val uuidGenerator: UuidGenerator,
) : AddChapterService {
    override fun add(request: RequestModel): ResponseModel {
        val (storyId, authorId, narrativeThreadId) = request
        checkPreconditions(storyId, authorId)
        val narrativeThread = fetchNarrativeThread(narrativeThreadId)

        val chapterId = uuidGenerator.randomUuid()
        val excerptId = uuidGenerator.randomUuid()

        val chapter = request.toChapter(chapterId)
        calculateLine(chapter, request)
        val excerpt = request.toExcerpt(excerptId, chapter)

        storyFragmentRepository.save(chapter.toDto())
        storyFragmentRepository.save(excerpt.toDto())

        saveFileService.save(request.content)

        return ResponseModel(
            chapter.toChapterDto(narrativeThread),
            excerpt.toExcerptDto(narrativeThread),
        )
    }

    private fun checkPreconditions(storyId: UUID, authorId: UUID) {
        if (authorRepository.existsById(authorId))
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

    private fun RequestModel.toChapter(id: UUID) = Chapter(
        id.toStoryFragmentId(), storyId.toStoryId(), authorId.toAuthorId(),
        narrativeThreadId?.toNarrativeThreadId(),
        title, summary,
        momentDate, momentTime,
        TimeLinePosition(line, x),
        null, null
    )

    private fun calculateLine(fragment: StoryFragment, request: RequestModel) {
        val lines = storyFragmentRepository.findAllByNarrativeThreadId(request.narrativeThreadId)
            .map { it.toEntity() }
            .groupBy { it.actualPosition.line }
        if (lines[fragment.actualPosition.line]?.none { it.isNear(fragment) } == true)
            return

        for ((line, points) in lines) {
            if (line == fragment.actualPosition.line) continue
            if (points.any { it.isNear(fragment) }) continue
            fragment.apply { placementPosition = placementPosition.copy(line = line) }
            return
        }

        val line = lines.keys.max() + 1
        fragment.apply { placementPosition = placementPosition.copy(line = line) }
    }

    private fun RequestModel.toExcerpt(id: UUID, chapter: Chapter) = Excerpt(
        id.toStoryFragmentId(), storyId.toStoryId(), authorId.toAuthorId(),
        narrativeThreadId?.toNarrativeThreadId(),
        excerptTitle, excerptSummary,
        momentDate, momentTime,
        chapter.actualPosition + .2,
        content.absolutePath,
        chapterId =  chapter.id,
    )

    private fun Chapter.toChapterDto(narrativeThread: NarrativeThread?) = ChapterDto(
        id.value, storyId.value, narrativeThreadId?.value,
        if (narrativeThread is VolumeThread) narrativeThread.volumeId.value else null,
        if (narrativeThread is CharacterBiographyThread) narrativeThread.characterId.value else null,
        title, "chapter",
        TimelinePositionDto(actualPosition.line, actualPosition.x),
        lastPosition.x - actualPosition.x,
    )

    private fun Excerpt.toExcerptDto(narrativeThread: NarrativeThread?) = ExcerptDto(
        id.value, storyId.value, narrativeThreadId?.value,
        if (narrativeThread is VolumeThread) narrativeThread.volumeId.value else null,
        if (narrativeThread is CharacterBiographyThread) narrativeThread.characterId.value else null,
        chapterId!!.value,
        title, "excerpt",
        TimelinePositionDto(actualPosition.line, actualPosition.x),
    )
}
