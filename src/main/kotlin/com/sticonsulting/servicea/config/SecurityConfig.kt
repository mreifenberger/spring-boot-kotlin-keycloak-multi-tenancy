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
        //TODO: permitAll for "/actuator/**", "/swagger-resources/**", "/favicon.ico"
        //TODO: configure oauth2Login with the relevant repositories
        //TODO: configure oauth2ResourceServer
        //TODO: add exception handling. retrieve registrationId and pass it to the SubdomainAuthEntryPointWrapper like: "/oauth2/authorization/$registrationId" P.S.: registrationId == realm
        //TODO: add saveOriginUrlFilter and make it the first filter in the chain

        return http.build()
    }
}