package com.lucassdalmeida.writing.application.fragment.create.impl

import com.lucassdalmeida.writing.application.fragment.create.CreateFragmentService
import com.lucassdalmeida.writing.application.fragment.create.CreateFragmentService.*
import com.lucassdalmeida.writing.application.fragment.repository.FragmentRepository
import com.lucassdalmeida.writing.application.fragment.repository.toDto
import com.lucassdalmeida.writing.domain.model.fragment.Excerpt
import com.lucassdalmeida.writing.domain.model.fragment.toExcerptId
import com.lucassdalmeida.writing.domain.service.UuidGenerator
import java.util.UUID

class CreateFragmentServiceImpl(
    private val repository: FragmentRepository,
    private val uuidGenerator: UuidGenerator,
) : CreateFragmentService {
    override fun create(request: RequestModel): UUID {
        val id = uuidGenerator.randomUuid()
        val fragment = request.toExcerpt(id)

        repository.save(fragment.toDto())
        return id
    }

    private fun RequestModel.toExcerpt(id: UUID) = Excerpt(id.toExcerptId(), title, fileUri, summary)
}