package com.lucassdalmeida.writing.application.fragments.find.impl

import com.lucassdalmeida.writing.application.fragments.find.FindFragmentService
import com.lucassdalmeida.writing.application.fragments.find.FindFragmentService.ResponseModel
import com.lucassdalmeida.writing.application.fragments.repository.StoryFragmentRepository
import com.lucassdalmeida.writing.application.fragments.repository.TimelinePositionDto
import com.lucassdalmeida.writing.application.fragments.repository.toEntity
import com.lucassdalmeida.writing.application.shared.exceptions.EntityNotFoundException
import com.lucassdalmeida.writing.application.thread.repository.NarrativeThreadRepository
import com.lucassdalmeida.writing.application.thread.repository.toEntity
import com.lucassdalmeida.writing.domain.model.fragment.Chapter
import com.lucassdalmeida.writing.domain.model.fragment.Excerpt
import com.lucassdalmeida.writing.domain.model.thread.CharacterBiographyThread
import com.lucassdalmeida.writing.domain.model.thread.VolumeThread
import java.util.UUID

class FindFragmentServiceImpl(
    private val storyFragmentRepository: StoryFragmentRepository,
    private val narrativeThreadRepository: NarrativeThreadRepository,
) : FindFragmentService {
    override fun findById(id: UUID): ResponseModel {
        val fragment = storyFragmentRepository.findById(id)
            ?.toEntity()
            ?: throw EntityNotFoundException("There is no fragment with id $id")
        val thread = fragment.narrativeThreadId
            ?.let {
                narrativeThreadRepository.findById(it.value)
                    ?.toEntity()
            }
        return with(fragment) {
            ResponseModel(
                id, storyId.value,
                narrativeThreadId?.value,
                if (thread is VolumeThread) thread.volumeId.value else null,
                if (thread is CharacterBiographyThread) thread.characterId.value else null,
                title,
                if (this is Excerpt) "excerpt" else "chapter",
                summary,
                momentDate, momentTime,
                TimelinePositionDto(actualPosition.line, actualPosition.x),
                if (this is Chapter) lastPosition.x - actualPosition.x else null,
                null,
            )
        }
    }
}