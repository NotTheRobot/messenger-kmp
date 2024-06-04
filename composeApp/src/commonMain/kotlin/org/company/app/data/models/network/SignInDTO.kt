package org.company.app.data.models.network

import kotlinx.serialization.Serializable

@Serializable
data class SignInDTO(
    val username: String,
    val password: String
)
