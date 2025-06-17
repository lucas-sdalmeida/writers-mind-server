package com.lucassdalmeida.writing.domain.model.timeline

import com.lucassdalmeida.writing.shared.Entity

class TimeLine(id: TimeLineId) : Entity<TimeLineId>(id) {
    private val _lineGroups = mutableListOf<LineGroup>()
}