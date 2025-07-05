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
import com.lucassdalmeida.writing.domain.model.fragment.TimeLinePosition
import com.lucassdalmeida.writing.domain.model.fragment.toStoryFragmentId
import com.lucassdalmeida.writing.domain.model.thread.CharacterBiographyThread
import com.lucassdalmeida.writing.domain.model.thread.NarrativeThread
import com.lucassdalmeida.writing.domain.model.thread.VolumeThread
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

        fragment.apply {
            narrativeThreadId = narrativeThread?.id
            placementPosition = TimeLinePosition(request.line, request.x)
            if (this is Excerpt) chapterId = null
        }
        calculateLine(fragment, request)

        storyFragmentRepository.save(fragment.toDto())

        val dto = fragment.toFragmentDto(request, narrativeThread)
        return ResponseModel(dto, null)
    }

    private fun fetchNarrativeThread(narrativeThreadId: UUID?): NarrativeThread? {
        if (narrativeThreadId == null) return null
        return narrativeThreadRepository
            .findById(narrativeThreadId)
            ?.toEntity()
            ?: throw EntityNotFoundException("The narrative thread of id $narrativeThreadId does not exist!")
    }

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

    private fun StoryFragment.toFragmentDto(
        request: RequestModel,
        narrativeThread: NarrativeThread?,
    ) = FragmentDto(
        request.id,
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
        val chapter = fetchChapter(chapterId)

        fragment.apply {
            narrativeThreadId = narrativeThread?.id
            placementPosition = TimeLinePosition(request.line, request.x)
            this.chapterId = chapterId.toStoryFragmentId()
        }
        chapter.addExcerpt(fragment)
        calculateLine(chapter, request)

        storyFragmentRepository.save(chapter.toDto())
        storyFragmentRepository.save(fragment.toDto())

        return ResponseModel(
            fragment.toFragmentDto(request, narrativeThread),
            chapter.toFragmentDto(request, narrativeThread)
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