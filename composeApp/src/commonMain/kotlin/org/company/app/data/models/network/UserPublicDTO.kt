package org.company.app.data.models.network

import db.UserDBO
import kotlinx.serialization.Serializable

@Serializable
data class UserPublicDTO(
    val username: String,
    val alterName: String,
    val imageRef: String?
)

fun UserPublicDTO.toUserDBO() = UserDBO(
    this.username,
    this.alterName,
    this.imageRef
)
