package com.lucassdalmeida.writing.domain.model.fragment

import com.lucassdalmeida.writing.domain.model.author.AuthorId
import com.lucassdalmeida.writing.domain.model.story.StoryId
import com.lucassdalmeida.writing.domain.model.thread.NarrativeThreadId
import com.lucassdalmeida.writing.shared.Notification
import java.time.LocalDate
import java.time.LocalTime

class Excerpt(
    id: StoryFragmentId,
    storyId: StoryId,
    authorId: AuthorId,
    narrativeThreadId: NarrativeThreadId?,
    title: String,
    summary: String?,
    momentDate: LocalDate?,
    momentTime: LocalTime?,
    placementPosition: TimeLinePosition,
    val fileUri: String,
    actualPosition: TimeLinePosition? = null,
    var chapterId: StoryFragmentId? = null,
) : StoryFragment(
    id, storyId, authorId, narrativeThreadId, title, summary, momentDate, momentTime, placementPosition, actualPosition,
) {
    override var lastPosition get() = actualPosition
        set(value) {}

    init {
        val notification = validate()
        require(notification.hasNoMessages()) { notification.toString() }
    }

    private fun validate() = Notification().also {
        if (fileUri.isBlank()) it.addMessagesFor("except", "Unable to create excerpt! The file URI must not be blank!")
    }
}