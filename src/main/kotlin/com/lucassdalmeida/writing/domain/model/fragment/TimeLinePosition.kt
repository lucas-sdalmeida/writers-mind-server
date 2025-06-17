package com.lucassdalmeida.writing.domain.model.fragment

import kotlin.math.abs

private const val MINIMUM_DISTANCE = 1.5

data class TimeLinePosition(val line: Int, val x: Double) {
    fun isNear(other: TimeLinePosition) = line == other.line
            && abs(x - other.x) <= MINIMUM_DISTANCE
}
