package com.lucassdalmeida.writing.domain.model.story

import com.lucassdalmeida.writing.domain.model.author.AuthorId
import com.lucassdalmeida.writing.shared.Entity
import com.lucassdalmeida.writing.shared.Notification

class Story(
    id: StoryId,
    title: String,
    objectives: String?,
    themes: String?,
    genres: String?,
    mainPlot: String?,
    setting: String?,
    summary: String?,
    coverImageUri: String?,
    val authorId: AuthorId,
) : Entity<StoryId>(id) {
    val title = title.trim()

    var objectives = if (objectives.isNullOrBlank()) null else objectives.trim()
        set(value) = run { field = if (value.isNullOrBlank()) null else value.trim() }

    var themes = if (themes.isNullOrBlank()) null else themes.trim()
        set(value) = run { field = if (value.isNullOrBlank()) null else value.trim() }

    var genres = if (genres.isNullOrBlank()) null else genres.trim()
        set(value) = run { field = if (value.isNullOrBlank()) null else value.trim() }

    var mainPlot = if (mainPlot.isNullOrBlank()) null else mainPlot.trim()
        set(value) = run { field = if (value.isNullOrBlank()) null else value.trim() }

    var setting = if (setting.isNullOrBlank()) null else setting.trim()
        set(value) = run { field = if (value.isNullOrBlank()) null else value.trim() }

    var summary = if (summary.isNullOrBlank()) null else summary.trim()
        set(value) = run { field = if (value.isNullOrBlank()) null else value.trim() }

    var coverImageUri = if (coverImageUri.isNullOrBlank()) null else coverImageUri.trim()
        set(value) = run { field = if (value.isNullOrBlank()) null else value.trim() }

    init {
        val notification = validate()
        require(notification.hasNoMessages()) { notification.toString() }
    }

    private fun validate() = Notification().also {
        if (title.isBlank())
            it.addMessagesFor("story", "Unable to create story! The title must not be blank!")
    }
}