package com.lucassdalmeida.writing.domain.model.thread

import com.lucassdalmeida.writing.domain.model.character.CharacterId

class CharacterBiographyThread(id: NarrativeThreadId, val characterId: CharacterId) : NarrativeThread(id)
