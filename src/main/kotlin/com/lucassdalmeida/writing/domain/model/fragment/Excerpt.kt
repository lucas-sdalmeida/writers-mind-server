package com.lucassdalmeida.writing.domain.model.fragment

import com.lucassdalmeida.writing.shared.Entity
import com.lucassdalmeida.writing.shared.Notification

class Excerpt(
    id: ExcerptId,
    title: String,
    val fileUri: String,
    var summary: String?
) : Entity<ExcerptId>(id) {
    var title = title
        set(value) {
            require(value.isNotBlank()) { "Unable to change excerpt title! The title must not be blank" }
            field = value
        }

    init {
        if (summary.isNullOrBlank()) summary = null
        val notification = validate()
        require(notification.hasNoMessages()) { notification.toString() }
    }

    private fun validate() = Notification().also {
        if (title.isBlank())
            it.addMessagesFor("excerpt", "Unable to create excerpt! The title must not be blank!")
        if (fileUri.isBlank())
            it.addMessagesFor("except", "Unable to create excerpt! The file URI must not be blank!")
    }
}