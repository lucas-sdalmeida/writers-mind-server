package com.lucassdalmeida.writing.domain.model.author

import com.lucassdalmeida.writing.domain.model.account.UserAccountId
import com.lucassdalmeida.writing.shared.Entity
import com.lucassdalmeida.writing.shared.Notification

class Author(
    id: AuthorId,
    name: String,
    pseudonym: String? = null,
    accountId: UserAccountId,
) : Entity<AuthorId>(id) {
    val name: String = name.trim()
    val pseudonym: String? = pseudonym?.trim()

    init {
        val notification = validate()
        require(notification.hasNoMessages()) { notification.toString() }
    }

    private fun validate() = Notification().also {
        if (name.isBlank())
            it.addMessagesFor("author", "Unable to create author! The name must not be blank!")
        if (pseudonym != null && pseudonym.isBlank())
            it.addMessagesFor("author", "Unable to create author! The pseudonym must not be blank!")
    }
}