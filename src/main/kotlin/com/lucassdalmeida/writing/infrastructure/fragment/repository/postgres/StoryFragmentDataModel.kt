package com.lucassdalmeida.writing.infrastructure.fragment.repository.postgres

import com.lucassdalmeida.writing.application.fragments.repository.StoryFragmentDto
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Entity
@Table(name = "story_fragment")
data class StoryFragmentDataModel(
    @Id val id: UUID,
    val storyId: UUID,
    val authorId: UUID,
    val narrativeThreadId: UUID?,
    val title: String,
    val summary: String?,
    val momentDate: LocalDate?,
    val momentTime: LocalTime?,
    val placementPositionLine: Int,
    val placementPositionX: Double,
    val actualPositionLine: Int,
    val actualPositionX: Double,
    val fileUri: String?,
    val chapterId: UUID?,
    val chapterLastPositionLine: Int?,
    val chapterLastPositionX: Double?,
)

fun StoryFragmentDto.toDataModel() = StoryFragmentDataModel(
    id, storyId, authorId, narrativeThreadId,
    title, summary,
    momentDate, momentTime,
    placementPositionLine, placementPositionX,
    actualPositionLine, actualPositionX,
    fileUri, chapterId,
    chapterLastPositionLine, chapterLastPositionX,
)

fun StoryFragmentDataModel.toDto() = StoryFragmentDto(
    id, storyId, authorId, narrativeThreadId,
    title, summary,
    momentDate, momentTime,
    placementPositionLine, placementPositionX,
    actualPositionLine, actualPositionX,
    fileUri, chapterId,
    chapterLastPositionLine, chapterLastPositionX,
)
