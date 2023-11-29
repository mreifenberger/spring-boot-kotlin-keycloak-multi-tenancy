package com.sticonsulting.servicea.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kc")
class KeycloakClientProperties {
    var realms: List<RealmConfigurationProperties> = mutableListOf()

    data class RealmConfigurationProperties(
        var subdomain: String,
        var baseUrl: String,
        var realm: String,
        var realmUrl: String,
        var clientId: String,
        var clientSecret: String,
    )
}
