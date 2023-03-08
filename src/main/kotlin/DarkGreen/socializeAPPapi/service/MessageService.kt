package DarkGreen.socializeAPPapi.service

import DarkGreen.socializeAPPapi.model.Message
import java.util.*

interface MessageService {
    fun create(message: Message): Message

    fun getAll(): List<Message>

    fun getById(id: Long): Optional<Message>

    fun update(id: Long, message: Message) : Optional<Message>

    fun delete( id: Long)
}