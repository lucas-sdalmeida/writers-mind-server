package com.lucassdalmeida.writing.domain.model.fragment

import com.lucassdalmeida.writing.domain.model.author.AuthorId
import com.lucassdalmeida.writing.domain.model.story.StoryId
import com.lucassdalmeida.writing.domain.model.thread.NarrativeThreadId
import com.lucassdalmeida.writing.shared.Entity
import com.lucassdalmeida.writing.shared.Notification
import java.time.LocalDate
import java.time.LocalTime

abstract class StoryFragment(
    id: StoryFragmentId,
    val storyId: StoryId,
    val authorId: AuthorId,
    val narrativeThreadId: NarrativeThreadId?,
    val title: String,
    summary: String?,
    val momentDate: LocalDate?,
    val momentTime: LocalTime?,
    placementPosition: TimeLinePosition,
    actualPosition: TimeLinePosition? = null,
) : Entity<StoryFragmentId>(id) {
    val summary = if (summary.isNullOrBlank()) null else summary

    var placementPosition = placementPosition
        set(value) {
            if (field == actualPosition) actualPosition = value

            if (value.line != actualPosition.line) actualPosition = actualPosition.copy(line = value.line)
            if (value.line != lastPosition.line) lastPosition = lastPosition.copy(line = value.line)

            field = value
        }
    var actualPosition = actualPosition ?: placementPosition
        private set

    abstract var lastPosition: TimeLinePosition
        protected set

    init {
        val notification = validate()
        require(notification.hasNoMessages()) { notification.toString() }
    }

    private fun validate() = Notification().also {
        if (title.isBlank())
            it.addMessagesFor("storyFragment", "A story fragment must not have a blank title!")
    }

    fun isNear(other: StoryFragment): Boolean {
        return actualPosition.isNear(other.actualPosition)
                || actualPosition.isNear(other.lastPosition)
                || actualPosition >= other.actualPosition && actualPosition <= other.lastPosition
                || lastPosition.isNear(other.actualPosition)
                || lastPosition.isNear(other.lastPosition)
                || lastPosition >= other.actualPosition && lastPosition <= other.lastPosition
    }
}
