package com.lucassdalmeida.writing.application.fragment.repository

import com.lucassdalmeida.writing.domain.model.fragment.Excerpt
import java.util.UUID

data class ExcerptDto(val id: UUID, val title: String, val fileUri: String, val summary: String?)

fun Excerpt.toDto() = ExcerptDto(id.value, title, fileUri, summary)
