package com.lucassdalmeida.writing.domain.model.fragment

import com.lucassdalmeida.writing.domain.model.author.AuthorId
import com.lucassdalmeida.writing.domain.model.story.StoryId
import com.lucassdalmeida.writing.domain.model.thread.NarrativeThreadId
import com.lucassdalmeida.writing.shared.Notification
import java.time.LocalDate
import java.time.LocalTime

class Chapter private constructor(
    id: StoryFragmentId,
    storyId: StoryId,
    authorId: AuthorId,
    narrativeThreadId: NarrativeThreadId?,
    title: String,
    summary: String?,
    momentDate: LocalDate?,
    momentTime: LocalTime?,
    placementPosition: TimeLinePosition,
    actualPosition: TimeLinePosition?,
    excerpts: List<StoryFragmentId>,
    lastPosition: TimeLinePosition?,
) : StoryFragment(
    id, storyId, authorId, narrativeThreadId, title, summary, momentDate, momentTime, placementPosition, actualPosition,
) {
    private val _excerpts = excerpts.toMutableList()
    val excerpts get() = _excerpts.toList()

    private var _lastPosition =
        if (lastPosition == null || lastPosition == placementPosition) placementPosition + 3.0 else lastPosition
    override val lastPosition get() = _lastPosition

    init {
        val notification = validate()
        require(notification.hasNoMessages()) { notification.toString() }
    }

    private fun validate() = Notification().also {
        if (_excerpts.isEmpty()) {
            it.addMessagesFor(
                "chapter",
                "The chapter must have at least one inner excerpt. Provided: $_excerpts",
            )
        }
    }

    constructor(
        id: StoryFragmentId,
        storyId: StoryId,
        authorId: AuthorId,
        narrativeThreadId: NarrativeThreadId,
        title: String,
        summary: String?,
        momentDate: LocalDate?,
        momentTime: LocalTime?,
        placementPosition: TimeLinePosition,
        actualPosition: TimeLinePosition?,
        excerpts: List<Excerpt>,
    ) : this(
        id, storyId, authorId, narrativeThreadId,
        title, summary,
        momentDate, momentTime,
        placementPosition, actualPosition,
        excerpts.map { it.id },
        excerpts.maxByOrNull { it.actualPosition }?.actualPosition,
    ) {
        val lines = excerpts.groupBy { it.actualPosition.line }
        val notification = Notification()

        if (lines.size > 1) {
            notification.addMessagesFor(
                "chapter",
                "All the chapter excerpts must be on the same line. Provided $excerpts",
            )
        }

        require(notification.hasNoMessages()) { notification.toString() }
    }

    fun addExcerpt(excerpt: Excerpt) {
        if (excerpt.id in _excerpts) return

        excerpt.apply {
            placementPosition = placementPosition.copy(line = placementPosition.line)
        }
        if (excerpt.placementPosition > _lastPosition) _lastPosition = excerpt.placementPosition + .5

        _excerpts.add(excerpt.id)
    }

    fun removeExcerpt(excerptId: StoryFragmentId) {
        _excerpts.remove(excerptId)
    }
}