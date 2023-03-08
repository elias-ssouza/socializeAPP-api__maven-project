package DarkGreen.socializeAPPapi.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity(name = "messages")
data class Message (
    @Id @GeneratedValue
    var id: Long? = null,
    var text: String
)