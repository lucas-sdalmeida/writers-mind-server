package com.lucassdalmeida.writing.domain.model.pack

import com.lucassdalmeida.writing.domain.model.fragment.StoryFragmentId

class ChapterPack(id: StoryPackId, val chapterId: StoryFragmentId) : StoryPack(id)
