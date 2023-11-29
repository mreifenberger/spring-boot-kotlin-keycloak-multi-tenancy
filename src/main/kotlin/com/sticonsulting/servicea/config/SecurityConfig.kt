package com.sticonsulting.servicea.config

import com.sticonsulting.servicea.config.filter.SaveOriginUrlFilter
import com.sticonsulting.servicea.config.filter.SubdomainRealmFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.server.ServerWebExchange

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    private val subdomainAuthenticationManagerResolver: ReactiveAuthenticationManagerResolver<ServerWebExchange>,
    private val clientRegistrationRepository: ReactiveClientRegistrationRepository,
    private val authorizedClientRepository: ServerOAuth2AuthorizedClientRepository,
) {

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http
            .authorizeExchange { authorize ->
                authorize.pathMatchers("/actuator/**", "/swagger-resources/**", "/favicon.ico").permitAll()
                    .anyExchange()
                    .authenticated()
            }
            .oauth2Login { oauth2Login ->
                oauth2Login.authorizedClientRepository(authorizedClientRepository)
                oauth2Login.clientRegistrationRepository(clientRegistrationRepository)
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.authenticationManagerResolver(subdomainAuthenticationManagerResolver)
            }
            .exceptionHandling { e ->
                e.authenticationEntryPoint { exchange, exception ->
                    val realmConfig = exchange.getAttribute<KeycloakClientProperties.RealmConfigurationProperties>(
                        SubdomainRealmFilter.REALM_CONFIG_ATTR_NAME
                    )
                    val registrationId = realmConfig?.realm
                    SubdomainAuthEntryPointWrapper("/oauth2/authorization/$registrationId").commence(
                        exchange,
                        exception
                    )
                }
            }
        http.addFilterAt(SaveOriginUrlFilter(), SecurityWebFiltersOrder.FIRST)
        return http.build()
    }
}