package com.lucassdalmeida.writing.domain.model.thread

import com.lucassdalmeida.writing.domain.model.character.CharacterId
import com.lucassdalmeida.writing.domain.model.story.StoryId

class CharacterBiographyThread(
    id: NarrativeThreadId,
    storyId: StoryId,
    val characterId: CharacterId,
) : NarrativeThread(id, storyId)
