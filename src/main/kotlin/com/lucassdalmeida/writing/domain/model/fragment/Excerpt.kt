package com.lucassdalmeida.writing.domain.model.fragment

import com.lucassdalmeida.writing.domain.model.author.AuthorId
import com.lucassdalmeida.writing.domain.model.pack.StoryPackId
import com.lucassdalmeida.writing.domain.model.story.StoryId
import com.lucassdalmeida.writing.shared.Notification

class Excerpt(
    id: StoryFragmentId,
    storyId: StoryId,
    authorId: AuthorId,
    storyPackId: StoryPackId,
    title: String,
    summary: String?,
    placementPosition: TimeLinePosition,
    actualPosition: TimeLinePosition? = null,
    val fileUri: String,
) : StoryFragment(id, storyId, authorId, storyPackId, title, summary, placementPosition, actualPosition) {
    override val lastPosition get() = actualPosition

    init {
        val notification = validate()
        require(notification.hasNoMessages()) { notification.toString() }
    }

    private fun validate() = Notification().also {
        if (fileUri.isBlank())
            it.addMessagesFor("except", "Unable to create excerpt! The file URI must not be blank!")
    }
}