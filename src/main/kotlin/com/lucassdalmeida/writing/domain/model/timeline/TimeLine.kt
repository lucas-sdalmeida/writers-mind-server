package com.lucassdalmeida.writing.domain.model.timeline

import com.lucassdalmeida.writing.domain.model.fragment.ExcerptId
import com.lucassdalmeida.writing.shared.Entity
import java.time.LocalDateTime
import java.util.TreeMap
import kotlin.math.abs

class TimeLine(id: TimeLineId) : Entity<TimeLineId>(id) {
    private val _lines = mutableListOf<MutableMap<Double, TimePoint>>()
    val lines get() = _lines
        .map { it.values.toList() }

    fun addPoint(point: TimePoint) {
        for (line in _lines) {
            if (line.keys.hasValueNear(point.pointX)) continue
            line[point.pointX] = point
            return
        }
        _lines.add(TreeMap(mutableMapOf(point.pointX to point)))
    }

    private fun Set<Double>.hasValueNear(pointX: Double) = any { abs(it - pointX) <= 1 }

    data class TimePoint(
        val fragmentId: ExcerptId,
        val title: String,
        val pointX: Double,
        val moment: LocalDateTime?,
    )
}