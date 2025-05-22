package com.lucassdalmeida.writing.shared

class Notification {
    private val messages = mutableMapOf<String, MutableList<Message>>()

    fun hasMessages() = !hasNoMessages()

    fun hasNoMessages() = messages.isEmpty()

    fun addMessagesFor(subject: String, message: String, cause: Throwable? = null) {
        addMessagesFor(subject, Message(message, cause))
    }

    fun addMessagesFor(subject: String, message: Message) {
        require(subject.isNotBlank()) { "Unable to add message to notification! The subject must not be blank!" }
        if (!messages.containsKey(subject)) messages[subject] = mutableListOf()
        messages[subject]!!.add(message)
    }

    override fun toString(): String {
        if (hasNoMessages()) return "{}"

        return buildString {
            append("{\n")

            messages.forEach { (subject, subjectMessages) ->
                append("    \"$subject\": ")
                append(subjectMessages.joinToString(",", "[ ", " ]") { "$it" })
                append(",\n")
            }

            delete(length - 2, length)
            append("\n}")
        }
    }

    data class Message(val message: String, val cause: Throwable?) {
        init {
            require(message.isNotBlank()) { "The notification message must not be null nor blank!" }
        }

        override fun toString(): String {
            if (cause != null) return "$message, caused by $cause"
            return message
        }
    }
}

fun String.causedBy(cause: Throwable) = Notification.Message(this, cause)
