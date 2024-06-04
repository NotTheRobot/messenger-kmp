package org.company.app.data.models.network

import kotlinx.serialization.Serializable

@Serializable
data class NewChatDTO(
    val chatName: String,
    val imageRef: String?,
    val users: List<String>
)
