package com.sticonsulting.servicea.config

import com.sticonsulting.servicea.config.filter.SubdomainRealmFilter
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver
import org.springframework.security.oauth2.jwt.JwtDecoders
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.concurrent.ConcurrentHashMap

@Component
class SubdomainAuthenticationManagerResolver :
    ReactiveAuthenticationManagerResolver<ServerWebExchange> {

    private val authenticationManagers = ConcurrentHashMap<String, ReactiveAuthenticationManager>()
    override fun resolve(context: ServerWebExchange): Mono<ReactiveAuthenticationManager> {
        val realmConfig =
            context.getAttribute<KeycloakClientProperties.RealmConfigurationProperties>(SubdomainRealmFilter.REALM_CONFIG_ATTR_NAME)
                ?: throw IllegalStateException("Realm configuration is missing from request attributes")

        return Mono.just(authenticationManager(realmConfig))
    }

    private fun authenticationManager(realmConfig: KeycloakClientProperties.RealmConfigurationProperties): ReactiveAuthenticationManager {
        return authenticationManagers.computeIfAbsent(realmConfig.realm) { _ ->
            JwtReactiveAuthenticationManager(JwtDecoders.fromIssuerLocation(realmConfig.realmUrl))
        }
    }
}
