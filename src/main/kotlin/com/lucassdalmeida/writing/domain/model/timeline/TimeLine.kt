package com.lucassdalmeida.writing.domain.model.timeline

import com.lucassdalmeida.writing.domain.model.fragment.ExcerptId
import com.lucassdalmeida.writing.shared.Entity
import java.time.LocalDateTime

class TimeLine(id: TimeLineId) : Entity<TimeLineId>(id) {
    private val lines = mutableListOf<MutableMap<Double, TimeMark>>()

    fun addMark(mark: TimeMark) {
        for (line in lines) {
            if (mark.position in line) continue
            line[mark.position] = mark
            return
        }
        lines.add(mutableMapOf(mark.position to mark))
    }

    fun removeMarkByExcerptId(excerptId: ExcerptId) {
        lines.forEach { line ->
            val mark = line.entries
                .find { (_, value) -> value.excerptId == excerptId }
            mark?.let { line.remove(it.key) }
        }
    }

    data class TimeMark(
        val excerptId: ExcerptId,
        val position: Double,
        val description: String?,
        val moment: LocalDateTime?,
    )
}