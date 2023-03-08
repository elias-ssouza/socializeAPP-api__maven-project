package DarkGreen.socializeAPPapi.service

import DarkGreen.socializeAPPapi.model.Message
import DarkGreen.socializeAPPapi.repository.MessageRepository
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.util.*

@Service
class MessageServiceImpl (private val repository: MessageRepository) : MessageService {
    override fun create(message: Message): Message {
        Assert.hasLength(message.text, "[text] não pode ser em branco!")
        Assert.isTrue(message.text.length >= 5, "[text] deve ter no mínimo 5 caracteres!")

        return repository.save(message)
    }

    override fun getAll(): List<Message> {
        return repository.findAll()
    }

    override fun getById(id: Long): Optional<Message> {
        return repository.findById(id)
    }

    override fun update(id: Long, message: Message): Optional<Message> {
        val optional = getById(id)
        if (optional.isEmpty) Optional.empty<Message>()

        return optional.map {
            val messageToUpdate = it.copy(
                text = message.text
            )
            repository.save(messageToUpdate)
        }
    }

    override fun delete(id: Long) {
        repository.findById(id).map{
            repository.delete(it)
        }.orElseThrow{throw RuntimeException("Id not found $id")}
    }
}