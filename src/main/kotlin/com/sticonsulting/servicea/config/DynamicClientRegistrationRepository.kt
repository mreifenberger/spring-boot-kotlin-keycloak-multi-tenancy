package com.sticonsulting.servicea.config

import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class DynamicClientRegistrationRepository(private var restTemplate: RestTemplate) : ClientRegistrationRepository {


    override fun findByRegistrationId(registrationId: String): ClientRegistration? {
        val tenant = TenantContext.getCurrentTenant()

        val clientConfig = fetchClientConfigForTenant(tenant)

        return ClientRegistration.withRegistrationId(registrationId)
                .clientId(clientConfig.clientId)
                .clientSecret(clientConfig.clientSecret)
                .build()
    }

    private fun fetchClientConfigForTenant(tenant: String): ClientConfig {
        val keycloakAdminUri = "http://keycloak-server/admin/realms/$tenant/clients"
        // Use a REST client to make the call to Keycloak
        // You'll need to authenticate this call with an admin user or service account

        val response = restTemplate.getForObject(keycloakAdminUri, ClientConfigResponse::class.java)
        // Process the response to extract the necessary configuration

        if (response != null) {
            return ClientConfig(
                clientId = response.clientId,
                clientSecret = response.clientSecret
                // ... other needed fields ...
            )
        }

        return ClientConfig("default","default")
    }

}
