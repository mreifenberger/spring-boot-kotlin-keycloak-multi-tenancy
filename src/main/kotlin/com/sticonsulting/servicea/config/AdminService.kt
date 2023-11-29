package com.sticonsulting.servicea.config

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class AdminService {




    fun fetchClientConfigForTenant(tenant: String): ClientRegistration {
        val accessToken = getKeycloakAdminToken()
        val keycloakAdminUri = "http://keycloak-server/admin/realms/$tenant/clients"

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set("Authorization", "Bearer $accessToken")
        }

        val entity = HttpEntity<String>(headers)
        val restTemplate = RestTemplate()

        val response = restTemplate.exchange(keycloakAdminUri, HttpMethod.GET, entity, Array<ClientRegistration>::class.java)

        // Look for the client named "backend-client"
        val clientRegistration = response.body?.firstOrNull { it.clientId == "backend-client" }
            ?: throw IllegalStateException("Client 'backend-client' not found")

        return clientRegistration
    }

    private fun getKeycloakAdminToken(): String {
        val keycloakServerUrl = "http://localhost:8080" +
                "" // Replace with your Keycloak server URL
        val realm = "master" // Replace with your realm
        val clientId = "admin-cli" // Replace with your admin client ID
        val clientSecret = "W27PkButJWCzGijPGAi7qvqNBu9Hd2GU4" // Replace with your client secret

        val restTemplate = RestTemplate()

        val uri = "$keycloakServerUrl/realms/$realm/protocol/openid-connect/token"

        val body: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>().apply {
            add("client_id", clientId)
            add("client_secret", clientSecret)
            add("grant_type", "client_credentials")
        }

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        val requestEntity = HttpEntity(body, headers)

        val response = restTemplate.postForEntity(uri, requestEntity, Map::class.java)
        val responseBody = response.body ?: throw IllegalStateException("No response body from Keycloak")

        return responseBody["access_token"] as? String ?: throw IllegalStateException("No access token in response")
    }
}