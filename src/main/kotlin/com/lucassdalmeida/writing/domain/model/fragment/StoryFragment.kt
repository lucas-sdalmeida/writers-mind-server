package com.lucassdalmeida.writing.domain.model.fragment

import com.lucassdalmeida.writing.shared.Entity
import com.lucassdalmeida.writing.shared.Notification
import java.time.LocalDate
import java.time.LocalTime

abstract class StoryFragment(
    id: StoryFragmentId,
    val title: String,
    val summary: String?,
    val positioningX: Double,
    val momentDate: LocalDate?,
    val momentTime: LocalTime?,
) : Entity<StoryFragmentId>(id) {
    abstract val lastX: Double

    init {
        val notification = validate()
        require(notification.hasNoMessages()) { notification.toString() }
    }

    private fun validate() = Notification().also {
        if (title.isBlank())
            it.addMessagesFor("storyFragment", "The title must not be blank!")
        if (summary != null && summary.isBlank())
            it.addMessagesFor("storyFragment", "The summary must not be blank!")
    }

    fun isNearOf(other: StoryFragment) = positioningX - other.positioningX < 1.5
            || other.lastX - lastX < 1.5
}
