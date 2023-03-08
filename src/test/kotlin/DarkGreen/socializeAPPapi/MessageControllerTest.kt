package DarkGreen.socializeAPPapi

import DarkGreen.socializeAPPapi.model.Message
import DarkGreen.socializeAPPapi.repository.MessageRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var messageRepository: MessageRepository

    @Test
    fun `test find all` () {

        messageRepository.save(Message(text = "apenas testando se tá tudo ok"))

        mockMvc.perform(MockMvcRequestBuilders.get("/messages"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].id").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].text").isString)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test find by id` () {

        val message = messageRepository.save(Message(text = "testando se tá tudo ok"))

        mockMvc.perform(MockMvcRequestBuilders.get("/messages/${message.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(message.id))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.text").value(message.text))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create message` () {
        val message = Message(text="Teste de post")
        val json = ObjectMapper().writeValueAsString(message)
        messageRepository.deleteAll()
        mockMvc.perform(
            MockMvcRequestBuilders.post("/messages")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.text").value(message.text))
            .andDo(MockMvcResultHandlers.print())

        Assertions.assertFalse(messageRepository.findAll().isEmpty())
    }

    @Test
    fun `test update message` (){
        val message = messageRepository.save(Message(text="Teste de update"))
            .copy(text = "Updated")
        val json = ObjectMapper().writeValueAsString(message)
        mockMvc.perform(
            MockMvcRequestBuilders.put("/messages/${message.id}")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.text").value(message.text))
            .andDo(MockMvcResultHandlers.print())

        val findById = messageRepository.findById(message.id!!)
        Assertions.assertTrue(findById.isPresent)
        Assertions.assertEquals(message.text, findById.get().text)
    }

    @Test
    fun `test delete message` (){
        val message = messageRepository.save(Message(text="Teste de update"))
            .copy(text = "Updated")
        mockMvc.perform(MockMvcRequestBuilders.delete("/messages/${message.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())

        val findById = messageRepository.findById(message.id!!)
        Assertions.assertFalse(findById.isPresent)
    }

    @Test
    fun `test create message validation error empty message` () {
        val message = Message(text="")
        val json = ObjectMapper().writeValueAsString(message)
        messageRepository.deleteAll()
        mockMvc.perform(
            MockMvcRequestBuilders.post("/messages")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[text] não pode ser em branco!"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create message validation error text should be 5 character` () {
        val message = Message(text="test")
        val json = ObjectMapper().writeValueAsString(message)
        messageRepository.deleteAll()
        mockMvc.perform(
            MockMvcRequestBuilders.post("/messages")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[text] deve ter no mínimo 5 caracteres!"))
            .andDo(MockMvcResultHandlers.print())
    }
}