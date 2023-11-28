package com.sticonsulting.servicea.config

data class ClientConfig(
    val clientId: String,
    val clientSecret: String,
)

data class ClientConfigResponse(
    val clientId: String,
    val clientSecret: String,
)
