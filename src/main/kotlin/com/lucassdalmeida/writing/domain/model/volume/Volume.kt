package com.lucassdalmeida.writing.domain.model.volume

import com.lucassdalmeida.writing.domain.model.story.StoryId
import com.lucassdalmeida.writing.shared.Entity

class Volume(id: VolumeId, val storyId: StoryId, val title: String, val summary: String?) : Entity<VolumeId>(id)
