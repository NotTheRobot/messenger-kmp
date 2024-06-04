package org.company.app.data.models.network

import kotlinx.serialization.Serializable

@Serializable
data class SecretDTO(val username: String, val secret: Long)
