package DarkGreen.socializeAPPapi.controller

import DarkGreen.socializeAPPapi.model.Message
import DarkGreen.socializeAPPapi.service.MessageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/messages")
class MessageController (private val service: MessageService){

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody message: Message): Message = service.create(message)

    @GetMapping
    fun getAll(): List<Message> = service. getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Message> =
        service.getById(id).map {
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody message: Message) : ResponseEntity<Message> =
        service.update(id, message).map {
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity<Void>(HttpStatus.OK)
    }
}