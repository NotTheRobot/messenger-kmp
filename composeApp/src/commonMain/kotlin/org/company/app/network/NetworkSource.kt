package org.company.app.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.company.app.data.models.network.*

class NetworkSource(private val client: HttpClient) {
     val host = "192.168.50.228"
     val port = 8080
     val base = "http://$host:$port/"
     var secret: Long? = null

    suspend fun sigUp(username: String, alterName: String, password: String, imageRef: String?): Boolean {
        val response = client.post(base + "createNewAccount") {
            contentType(ContentType.Application.Json)
            setBody(UserPrivateDTO(username, alterName, password, imageRef))
        }
        if (response.status.value in 200..299) {
            return true
        } else {
            return false
        }
    }
    suspend fun signIn(username: String, password: String): Boolean{
        val response = client.post(base + "signIn"){
            contentType(ContentType.Application.Json)
            setBody(SignInDTO(username, password))
        }
        if(response.status.value in 200..299){
            secret = response.bodyAsText().toLong()
            println(secret)
            println("signIn success")
            return true
        }else{
            println("signIn failed")
            return false
        }
    }
    suspend fun createNewChat(newChatDTO: NewChatDTO): Boolean{
        val response = client.post(base + "createNewChat"){
            contentType(ContentType.Application.Json)
            setBody(newChatDTO)
        }

        if(response.status.value in 200..299){
            println("New chat created")
            return true
        }else{
            println("Error: Cannot create new chat")
            return false
        }
    }

    suspend fun getAllChats(username: String): List<ChatDTO>{
        if (secret == null){
            return listOf()
        }
        val response = client.post(base + "getChatsByUser"){
            contentType(ContentType.Application.Json)
            setBody(SecretDTO(username, secret!!))
        }

        println("getAllChats: ${response.bodyAsText()}")
        println("getAllChats status: ${response.status}")
        return Json.decodeFromString<List<ChatDTO>>(response.bodyAsText())
    }

    suspend fun getAllMessages(username: String): List<List<RegisteredMessageDTO>>{

        val response = client.post(base + "getAllMessages"){
            contentType(ContentType.Application.Json)
            setBody(SecretDTO(username, secret!!))
        }
        if(response.status.value in 200..299){
            val result = Json.decodeFromString<List<List<RegisteredMessageDTO>>>(response.bodyAsText())
            println("getAllMessages success with:")
            println(result.toString())
            return result
        }else{
            println("getAllMessages failed")
            return listOf()
        }
    }

    suspend fun getAllUsers(username: String): List<UserPublicDTO>{
        if (secret == null){
            return listOf()
        }
        val response = client.post(base + "getAllUsers"){
            contentType(ContentType.Application.Json)
            setBody(SecretDTO(username, secret!!))
        }
        if(response.status.value in 200..299){
            return Json.decodeFromString<List<UserPublicDTO>>(response.bodyAsText())
        }else{
            println("getAllUsers failed")
            return listOf()
        }
    }

    suspend fun getUserByUsername(username: String): UserPublicDTO{
         val response = client.post(base + "getUserByUsername"){
            setBody(username)
         }
        return response.body<UserPublicDTO>()
    }

    suspend fun getChatById(chatId: String): ChatDTO{
        val response = client.post(base + "getChatById"){
            setBody(chatId)
        }
        return response.body<ChatDTO>()
    }
}
