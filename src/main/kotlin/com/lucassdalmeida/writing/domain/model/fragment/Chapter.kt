package com.lucassdalmeida.writing.domain.model.fragment

import com.lucassdalmeida.writing.domain.model.author.AuthorId
import com.lucassdalmeida.writing.domain.model.story.StoryId
import com.lucassdalmeida.writing.domain.model.thread.NarrativeThreadId
import com.lucassdalmeida.writing.shared.Notification
import java.time.LocalDate
import java.time.LocalTime

class Chapter(
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
    lastPosition: TimeLinePosition?,
) : StoryFragment(
    id, storyId, authorId, narrativeThreadId, title, summary, momentDate, momentTime, placementPosition, actualPosition,
) {
    private var _lastPosition = lastPosition ?: (placementPosition + 3.0)
    override val lastPosition get() = _lastPosition

    fun addExcerpt(excerpt: Excerpt) {
        require(excerpt.placementPosition.line == placementPosition.line) {
            "The excerpt must be on the same line as its chapter!"
        }
        if (excerpt.placementPosition > _lastPosition) _lastPosition = excerpt.placementPosition + .5
    }
}