package com.lucassdalmeida.writing.domain.model.fragment

import kotlin.math.abs

private const val MINIMUM_DISTANCE = .5

data class TimeLinePosition(val line: Int, val x: Double) : Comparable<TimeLinePosition> {
    fun isNear(other: TimeLinePosition) =
        line == other.line && isHorizontallyNear(other)

    fun isHorizontallyNear(other: TimeLinePosition) = abs(x - other.x) <= MINIMUM_DISTANCE

    override operator fun compareTo(other: TimeLinePosition) =
        if (x != other.x) x.compareTo(other.x) else line.compareTo(other.line)

    operator fun plus(value: Double) = copy(x = x + value)
}
