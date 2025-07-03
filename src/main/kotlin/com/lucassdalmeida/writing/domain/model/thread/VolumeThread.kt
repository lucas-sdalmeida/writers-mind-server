package com.lucassdalmeida.writing.domain.model.thread

import com.lucassdalmeida.writing.domain.model.story.StoryId
import com.lucassdalmeida.writing.domain.model.volume.VolumeId

class VolumeThread(id: NarrativeThreadId, storyId: StoryId, val volumeId: VolumeId) : NarrativeThread(id, storyId)
