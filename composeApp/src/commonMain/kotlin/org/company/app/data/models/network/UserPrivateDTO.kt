package org.company.app.data.models.network

import kotlinx.serialization.Serializable

@Serializable
data class UserPrivateDTO(
    val username: String,
    val alterName: String,
    val password: String,
    val imageRef: String?,
)


