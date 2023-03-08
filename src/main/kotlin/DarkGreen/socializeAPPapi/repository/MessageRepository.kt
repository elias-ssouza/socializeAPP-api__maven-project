package DarkGreen.socializeAPPapi.repository

import DarkGreen.socializeAPPapi.model.Message
import org.springframework.data.jpa.repository.JpaRepository

interface  MessageRepository: JpaRepository<Message, Long> {
}