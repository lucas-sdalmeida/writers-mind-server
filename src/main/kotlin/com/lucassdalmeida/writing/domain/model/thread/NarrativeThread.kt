package com.lucassdalmeida.writing.domain.model.thread

import com.lucassdalmeida.writing.domain.model.story.StoryId
import com.lucassdalmeida.writing.shared.Entity

abstract class NarrativeThread(id: NarrativeThreadId, val storyId: StoryId) : Entity<NarrativeThreadId>(id)
