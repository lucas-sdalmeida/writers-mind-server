package com.lucassdalmeida.writing.domain.model.character

import com.lucassdalmeida.writing.shared.Entity
import com.lucassdalmeida.writing.shared.Notification

class Character(
    id: CharacterId,
    name: String,
    attributes: Map<String, String>,
    contradictions: List<Contradiction>,
) : Entity<CharacterId>(id) {
    var name = name.trim()
        set(value) {
            val v = value.trim()
            require(v.isNotBlank()) { "Unable to update the character's name: the name must not be blank!" }
            field = v
        }

    private val _attributes = attributes.toMutableMap()
    val attributes get() = _attributes.toMap()

    private val _contradictions = contradictions.toMutableSet()
    val contradictions get() = _contradictions.toMutableList()

    init {
        val notification = Notification()
        require(notification.hasNoMessages()) { notification.toString() }
    }

    private fun validate() = Notification().also {
        if (name.isBlank()) {
            it.addMessagesFor("character", "Unable to create Character! The name")
        }
        if (_attributes.containsKey("name")) {
            it.addMessagesFor(
                "character",
                "Invalid attribute! You can't add the character's name twice. Attributes: $_attributes",
            )
        }
    }
}