package com.sticonsulting.servicea.config.filter

import com.sticonsulting.servicea.config.KeycloakClientProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrations
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
@EnableConfigurationProperties(KeycloakClientProperties::class)
@Order(Ordered.HIGHEST_PRECEDENCE)
class SubdomainRealmFilter(
    private val keycloakClientProperties: KeycloakClientProperties,
    //private val dynamicClientRegistrationRepository: DynamicClientRegistrationRepository
) : WebFilter {

    companion object {
        const val REALM_CONFIG_ATTR_NAME = "realmConfig"
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val subdomain = exchange.request.uri.host.substringBefore('.')
        exchange.attributes[REALM_CONFIG_ATTR_NAME] = getCurrentKeycloakConfiguration(subdomain)
        return chain.filter(exchange)
    }

    @Bean
    fun clientRegistrationRepository(): ReactiveClientRegistrationRepository {
        val registrations: List<ClientRegistration> = keycloakClientProperties.realms.map { realm ->
            createClientRegistration(realm)
        }
        return InMemoryReactiveClientRegistrationRepository(registrations)
    }

    @Bean
    fun authorizedClientRepository(): ServerOAuth2AuthorizedClientRepository {
        return WebSessionServerOAuth2AuthorizedClientRepository()
    }

    private fun createClientRegistration(realm: KeycloakClientProperties.RealmConfigurationProperties): ClientRegistration {
        return ClientRegistrations.fromOidcIssuerLocation(realm.realmUrl)
            .registrationId(realm.realm)
            .clientId(realm.clientId)
            .clientSecret(realm.clientSecret)
            .scope(
                "openid",
                "address",
                "email",
                "microprofile-jwt",
                "offline_access",
                "phone",
                "profile",
                "roles"
            )
            .jwkSetUri("${realm.realmUrl}/protocol/openid-connect/certs")
            .build()
    }

    private fun getCurrentKeycloakConfiguration(
        subdomain: String,
    ): KeycloakClientProperties.RealmConfigurationProperties? {
        // find the matching realm or return the default (first) one if not found
        return keycloakClientProperties.realms.find { it.subdomain.equals(subdomain, true) }
            ?: keycloakClientProperties.realms.firstOrNull()
    }
}
