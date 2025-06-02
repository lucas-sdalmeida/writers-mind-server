package com.lucassdalmeida.writing.application.fragment.create

import java.util.UUID

interface CreateFragmentService {
    fun create(request: RequestModel): UUID

    data class RequestModel(val title: String, val fileUri: String, val summary: String?)
}