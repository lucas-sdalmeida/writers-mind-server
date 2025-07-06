package com.lucassdalmeida.writing.application.fragments.move.impl

import com.lucassdalmeida.writing.application.fragments.move.MoveFragmentService
import com.lucassdalmeida.writing.application.fragments.move.MoveFragmentService.RequestModel
import com.lucassdalmeida.writing.application.fragments.move.MoveFragmentService.ResponseModel
import com.lucassdalmeida.writing.application.fragments.move.MoveFragmentService.FragmentDto
import com.lucassdalmeida.writing.application.fragments.repository.StoryFragmentRepository
import com.lucassdalmeida.writing.application.fragments.repository.TimelinePositionDto
import com.lucassdalmeida.writing.application.fragments.repository.toDto
import com.lucassdalmeida.writing.application.fragments.repository.toEntity
import com.lucassdalmeida.writing.application.shared.exceptions.EntityNotFoundException
import com.lucassdalmeida.writing.application.thread.repository.NarrativeThreadRepository
import com.lucassdalmeida.writing.application.thread.repository.toEntity
import com.lucassdalmeida.writing.domain.model.fragment.Chapter
import com.lucassdalmeida.writing.domain.model.fragment.Excerpt
import com.lucassdalmeida.writing.domain.model.fragment.StoryFragment
import com.lucassdalmeida.writing.domain.model.fragment.StoryFragmentId
import com.lucassdalmeida.writing.domain.model.fragment.TimeLinePosition
import com.lucassdalmeida.writing.domain.model.fragment.toStoryFragmentId
import com.lucassdalmeida.writing.domain.model.thread.CharacterBiographyThread
import com.lucassdalmeida.writing.domain.model.thread.NarrativeThread
import com.lucassdalmeida.writing.domain.model.thread.VolumeThread
import com.lucassdalmeida.writing.domain.model.thread.toNarrativeThreadId
import java.util.*

class MoveFragmentServiceImpl(
    private val storyFragmentRepository: StoryFragmentRepository,
    private val narrativeThreadRepository: NarrativeThreadRepository,
) : MoveFragmentService {
    override fun move(request: RequestModel): ResponseModel {
        val fragment = storyFragmentRepository.findById(request.id)
            ?.toEntity()
            ?: throw EntityNotFoundException("There's no fragment with id ${request.id}!")

        val narrativeThread = fetchNarrativeThread(request.narrativeThreadId)
        val lines = storyFragmentRepository.findAllByNarrativeThreadId(request.narrativeThreadId)
            .map { it.toEntity() }
            .groupBy { it.actualPosition.line }
        val chapterOldLine = fragment.actualPosition.line

        fragment.apply {
            narrativeThreadId = narrativeThread?.id
            placementPosition = TimeLinePosition(request.line, placementPosition.x + request.deltaX)
            if (this is Excerpt) chapterId = null
        }
        calculateLine(fragment, request, lines)

        val movedFragments = mutableListOf(fragment)
        if (fragment is Chapter)
            movedFragments.addAll(updateChapterFragments(fragment, chapterOldLine, request, lines))

        movedFragments.forEach {
            storyFragmentRepository.save(it.toDto())
        }

        return ResponseModel(
            movedFragments
                .map { it.toFragmentDto(narrativeThread) }
                .associateBy { it.id }
        )
    }

    private fun fetchNarrativeThread(narrativeThreadId: UUID?): NarrativeThread? {
        if (narrativeThreadId == null) return null
        return narrativeThreadRepository
            .findById(narrativeThreadId)
            ?.toEntity()
            ?: throw EntityNotFoundException("The narrative thread of id $narrativeThreadId does not exist!")
    }

    private fun calculateLine(fragment: StoryFragment, request: RequestModel, lines: Map<Int, List<StoryFragment>>) {
        if (lines[fragment.actualPosition.line] == null)
            return
        if (lines[fragment.actualPosition.line]?.none { areColliding(fragment, it) } == true)
            return

        for ((line, points) in lines) {
            if (line == fragment.actualPosition.line) continue

            fragment.apply { placementPosition = placementPosition.copy(line = line) }
            if (points.any { areColliding(it, fragment) }) continue

            return
        }

        val line = lines.keys.max() + 1
        fragment.apply { placementPosition = placementPosition.copy(line = line) }
    }

    private fun updateChapterFragments(
        chapter: Chapter,
        chapterOldLine: Int,
        request: RequestModel,
        lines: Map<Int, List<StoryFragment>>,
    ): List<StoryFragment> {
        return lines[chapterOldLine]!!
            .filter { it is Excerpt && it.chapterId == chapter.id }
            .map {
                it.apply {
                    narrativeThreadId = request.narrativeThreadId?.toNarrativeThreadId()
                    placementPosition = TimeLinePosition(
                        chapter.actualPosition.line,
                        placementPosition.x + request.deltaX
                    )
                }
            }
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

    private fun StoryFragment.toFragmentDto(
        narrativeThread: NarrativeThread?,
    ) = FragmentDto(
        id.value,
        storyId.value,
        narrativeThread?.id?.value,
        if (narrativeThread is VolumeThread) narrativeThread.volumeId.value else null,
        if (narrativeThread is CharacterBiographyThread) narrativeThread.characterId.value else null,
        if (this is Excerpt) chapterId?.value else null,
        title,
        if (this is Excerpt) "excerpt" else "chapter",
        TimelinePositionDto(actualPosition.line, actualPosition.x),
        if (this is Chapter) lastPosition.x - actualPosition.x else null,
    )

    override fun moveToChapter(chapterId: UUID, request: RequestModel): ResponseModel {
        val fragment = storyFragmentRepository.findById(request.id)
            ?.toEntity()
            ?: throw EntityNotFoundException("There's no fragment with id ${request.id}!")
        require(fragment is Excerpt) { "Unable to add a chapter to another one! ${request.id} is a chapter!" }

        val narrativeThread = fetchNarrativeThread(request.narrativeThreadId)
        val lines = storyFragmentRepository.findAllByNarrativeThreadId(request.narrativeThreadId)
            .map { it.toEntity() }
            .groupBy { it.actualPosition.line }
        val chapter = fetchChapter(chapterId)

        fragment.apply {
            narrativeThreadId = narrativeThread?.id
            placementPosition = TimeLinePosition(request.line, placementPosition.x + request.deltaX)
            this.chapterId = chapterId.toStoryFragmentId()
        }
        chapter.addExcerpt(fragment)
        calculateLine(chapter, request, lines)

        storyFragmentRepository.save(chapter.toDto())
        storyFragmentRepository.save(fragment.toDto())

        return ResponseModel(
            mutableMapOf(
                fragment.id.value to fragment.toFragmentDto(narrativeThread),
                chapter.id.value to chapter.toFragmentDto(narrativeThread),
            )
        )
    }

    private fun fetchChapter(chapterId: UUID): Chapter {
        val fragment = storyFragmentRepository.findById(chapterId)
            ?.toEntity()
            ?: throw EntityNotFoundException("There is no chapter with id $chapterId")
        require(fragment is Chapter) { "The fragment of id $chapterId is not a chapter!" }
        return fragment
    }
}