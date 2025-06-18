package com.lucassdalmeida.writing.domain.model.fragment

import kotlin.math.abs

private const val MINIMUM_DISTANCE = 1.5

data class TimeLinePosition(val line: Int, val x: Double) : Comparable<TimeLinePosition> {
    fun isNear(other: TimeLinePosition) =
        line == other.line && isHorizontallyNear(other)

    fun isHorizontallyNear(other: TimeLinePosition) = abs(x - other.x) <= MINIMUM_DISTANCE

    override fun compareTo(other: TimeLinePosition) =
        if (line != other.line) line.compareTo(other.line) else x.compareTo(other.x)
}
