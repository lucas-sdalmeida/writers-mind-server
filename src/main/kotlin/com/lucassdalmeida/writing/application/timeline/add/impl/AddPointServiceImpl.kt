package com.lucassdalmeida.writing.application.timeline.add.impl

import com.lucassdalmeida.writing.application.fragment.create.CreateFragmentService
import com.lucassdalmeida.writing.application.shared.exceptions.EntityNotFoundException
import com.lucassdalmeida.writing.application.timeline.add.AddPointService
import com.lucassdalmeida.writing.application.timeline.add.AddPointService.RequestModel
import com.lucassdalmeida.writing.application.timeline.repository.TimeLineRepository
import com.lucassdalmeida.writing.application.timeline.repository.toDto
import com.lucassdalmeida.writing.application.timeline.repository.toTimeLine
import com.lucassdalmeida.writing.domain.model.fragment.toExcerptId
import com.lucassdalmeida.writing.domain.model.timeline.TimeLine.TimePoint
import com.lucassdalmeida.writing.application.fragment.create.CreateFragmentService.RequestModel as FragmentRequest

class AddPointServiceImpl(
    private val repository: TimeLineRepository,
    private val createFragmentService: CreateFragmentService,
) : AddPointService {
    override fun addPoint(request: RequestModel) {
        val (timeLineId, title, fileUri, summary) = request
        val timeLine = repository.findById(timeLineId)
            ?.toTimeLine()
            ?: throw EntityNotFoundException("Unable to find time line for story: $timeLineId!")

        val fragmentRequest = FragmentRequest(title, fileUri, summary)
        val fragmentId = createFragmentService.create(fragmentRequest)

        val point =
            TimePoint(fragmentId.toExcerptId(), title, request.pointX, request.moment)
        timeLine.addPoint(point)

        repository.save(timeLine.toDto())
    }
}