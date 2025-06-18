package com.lucassdalmeida.writing.domain.model.fragment

import com.lucassdalmeida.writing.domain.model.author.AuthorId
import com.lucassdalmeida.writing.domain.model.pack.StoryPackId
import com.lucassdalmeida.writing.domain.model.story.StoryId
import com.lucassdalmeida.writing.shared.Notification

class Chapter(
    id: StoryFragmentId,
    storyId: StoryId,
    authorId: AuthorId,
    storyPackId: StoryPackId,
    title: String,
    summary: String?,
    placementPosition: TimeLinePosition,
    actualPosition: TimeLinePosition? = null,
    excerpts: List<Excerpt>,
) : StoryFragment(id, storyId, authorId, storyPackId, title, summary, placementPosition, actualPosition) {
    private val _excerpts = excerpts.toMutableList()
    val excerpts get() = _excerpts.toList()

    override val lastPosition get() = _excerpts.last().actualPosition

    init {
        val notification = validate()
        require(notification.hasNoMessages()) { notification.toString() }
        _excerpts.sortBy { it.actualPosition }
    }

    private fun validate() = Notification().also {
        if (_excerpts.isEmpty())
            it.addMessagesFor("chapter", "The chapter must have at least one inner excerpt. Provided: $_excerpts")

        val lines = _excerpts.distinctBy { e -> e.actualPosition.line }
        if (lines.size > 1)
            it.addMessagesFor("chapter", "All excerpts of a chapter must be on the same line. Excerpts: $_excerpts; Lines: $lines")
    }
}