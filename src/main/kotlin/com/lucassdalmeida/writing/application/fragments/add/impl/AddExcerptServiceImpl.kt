package com.lucassdalmeida.writing.application.fragments.add.impl

import com.lucassdalmeida.writing.application.author.repository.AuthorRepository
import com.lucassdalmeida.writing.application.fragments.add.AddExcerptService
import com.lucassdalmeida.writing.application.fragments.add.AddExcerptService.RequestModel
import com.lucassdalmeida.writing.application.fragments.file.SaveFileService
import com.lucassdalmeida.writing.application.fragments.repository.StoryFragmentDto
import com.lucassdalmeida.writing.application.fragments.repository.StoryFragmentRepository
import com.lucassdalmeida.writing.application.fragments.repository.toDto
import com.lucassdalmeida.writing.application.shared.exceptions.EntityNotFoundException
import com.lucassdalmeida.writing.application.shared.exceptions.UnauthenticatedUserException
import com.lucassdalmeida.writing.application.story.repository.StoryRepository
import com.lucassdalmeida.writing.application.story.repository.toStory
import com.lucassdalmeida.writing.domain.model.author.toAuthorId
import com.lucassdalmeida.writing.domain.model.fragment.Chapter
import com.lucassdalmeida.writing.domain.model.fragment.Excerpt
import com.lucassdalmeida.writing.domain.model.fragment.StoryFragment
import com.lucassdalmeida.writing.domain.model.fragment.TimeLinePosition
import com.lucassdalmeida.writing.domain.model.fragment.toStoryFragmentId
import com.lucassdalmeida.writing.domain.model.pack.toStoryPackId
import com.lucassdalmeida.writing.domain.model.story.toStoryId
import com.lucassdalmeida.writing.domain.service.UuidGenerator
import java.util.UUID

class AddExcerptServiceImpl(
    private val storyFragmentRepository: StoryFragmentRepository,
    private val storyRepository: StoryRepository,
    private val authorRepository: AuthorRepository,
    private val saveFileService: SaveFileService,
    private val uuidGenerator: UuidGenerator,
) : AddExcerptService {
    override fun add(request: RequestModel): UUID {
        val (storyId, authorId) = request
        checkPreconditions(storyId, authorId)

        val id = uuidGenerator.randomUuid()
        val excerpt = request.toExcerpt(id)

        val fragments = storyFragmentRepository
            .findAllByPackId(request.packId)
            .map(::mapFragmentDtoToEntity)
        val actualLine = calculatePlacementLine(excerpt, fragments)

        if (request.line != actualLine)
            excerpt.placementPosition = excerpt.placementPosition.copy(line = actualLine)

        saveFileService.save(request.content)
        storyFragmentRepository.save(excerpt.toDto())

        return id
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

    private fun RequestModel.toExcerpt(id: UUID) = Excerpt(
        id.toStoryFragmentId(),
        storyId.toStoryId(), authorId.toAuthorId(), packId.toStoryPackId(),
        title, summary,
        momentDate, momentTime,
        TimeLinePosition(line, x),
        fileUri = content.absolutePath,
    )

    private fun mapFragmentDtoToEntity(dto: StoryFragmentDto): StoryFragment {
        if (dto.fileUri != null) return dto.toExcerpt()

        val chapterExcerpts = storyFragmentRepository
            .findAllByChapter(dto.id)
            .map { mapFragmentDtoToEntity(it) as Excerpt }
        return dto.toChapter(chapterExcerpts)
    }

    private fun StoryFragmentDto.toExcerpt() = Excerpt(
        id.toStoryFragmentId(),
        storyId.toStoryId(), authorId.toAuthorId(), packId.toStoryPackId(),
        title, summary,
        momentDate, momentTime,
        TimeLinePosition(placementPositionLine, placementPositionX),
        TimeLinePosition(actualPositionLine, actualPositionX),
        fileUri!!,
    )

    private fun StoryFragmentDto.toChapter(excerpts: List<Excerpt>) = Chapter(
        id.toStoryFragmentId(),
        storyId.toStoryId(), authorId.toAuthorId(), packId.toStoryPackId(),
        title, summary,
        momentDate, momentTime,
        TimeLinePosition(placementPositionLine, placementPositionX),
        TimeLinePosition(actualPositionLine, actualPositionX),
        excerpts,
    )

    private fun calculatePlacementLine(excerpt: Excerpt, fragments: List<StoryFragment>): Int {
        val requestLine = excerpt.actualPosition.line
        val lines = fragments.groupBy { it.actualPosition.line }
        if (lines[requestLine]?.any { it.isNear(excerpt) } == false) return requestLine

        for ((line, fragments) in lines) {
            if (line == requestLine || fragments.any { it.isNear(excerpt) }) continue
            return line
        }

        return lines.size + 1
    }
}