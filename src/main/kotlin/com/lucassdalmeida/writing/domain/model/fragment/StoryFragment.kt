package com.lucassdalmeida.writing.domain.model.fragment

import com.lucassdalmeida.writing.shared.Entity
import com.lucassdalmeida.writing.shared.Notification

abstract class StoryFragment(
    id: StoryFragmentId,
    val title: String,
    val summary: String?,
    val placementPosition: TimeLinePosition,
    actualPosition: TimeLinePosition? = null,
) : Entity<StoryFragmentId>(id) {
    val actualPosition = actualPosition ?: placementPosition
    abstract val lastPosition: TimeLinePosition

    init {
        val notification = validate()
        require(notification.hasNoMessages()) { notification.toString() }
    }

    private fun validate() = Notification().also {
        if (title.isBlank())
            it.addMessagesFor("storyFragment", "A story fragment must not have a blank title!")
        if (summary != null && summary.isBlank())
            it.addMessagesFor("storyFragment", "A story fragment summary, when provided, must not be blank!")
    }

    fun isNear(other: StoryFragment) = actualPosition.isNear(other.actualPosition)
            || lastPosition.isNear(other.lastPosition)
}
