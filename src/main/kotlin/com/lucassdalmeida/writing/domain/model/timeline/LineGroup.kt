package com.lucassdalmeida.writing.domain.model.timeline

import com.lucassdalmeida.writing.domain.model.fragment.StoryFragment
import com.lucassdalmeida.writing.shared.Entity

class LineGroup(id: LineGroupId) : Entity<LineGroupId>(id) {
    private val _lines = mutableListOf<MutableList<StoryFragment>>()

    fun addFragment(storyFragment: StoryFragment) {
        for (line in _lines) {
            if (line.any { it.isNearOf(storyFragment) }) continue
            line.add(storyFragment)
            return
        }
        _lines.add(mutableListOf(storyFragment))
    }
}
