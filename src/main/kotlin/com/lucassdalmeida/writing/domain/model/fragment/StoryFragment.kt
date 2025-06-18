package com.lucassdalmeida.writing.domain.model.fragment

import com.lucassdalmeida.writing.domain.model.author.AuthorId
import com.lucassdalmeida.writing.domain.model.pack.StoryPack
import com.lucassdalmeida.writing.domain.model.pack.StoryPackId
import com.lucassdalmeida.writing.domain.model.story.StoryId
import com.lucassdalmeida.writing.shared.Entity
import com.lucassdalmeida.writing.shared.Notification

abstract class StoryFragment(
    id: StoryFragmentId,
    storyId: StoryId,
    authorId: AuthorId,
    val storyPackId: StoryPackId,
    val title: String,
    summary: String?,
    val placementPosition: TimeLinePosition,
    actualPosition: TimeLinePosition? = null,
) : Entity<StoryFragmentId>(id) {
    val summary = if (summary.isNullOrBlank()) null else summary
    val actualPosition = actualPosition ?: placementPosition
    abstract val lastPosition: TimeLinePosition

    init {
        val notification = validate()
        require(notification.hasNoMessages()) { notification.toString() }
    }

    private fun validate() = Notification().also {
        if (title.isBlank())
            it.addMessagesFor("storyFragment", "A story fragment must not have a blank title!")
    }

    fun isNear(other: StoryFragment) = actualPosition.isNear(other.actualPosition)
            || lastPosition.isNear(other.lastPosition)
}
