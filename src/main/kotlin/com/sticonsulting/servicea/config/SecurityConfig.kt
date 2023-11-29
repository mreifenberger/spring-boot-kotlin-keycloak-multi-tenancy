package com.sticonsulting.servicea.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.client.RestTemplate

@Configuration
@EnableWebSecurity
class SecurityConfig(    private val tenantIdentificationFilter: TenantIdentificationFilter,
                         private val clientRegistrationRepository: DynamicClientRegistrationRepository) {

    @Bean
    fun securityFilterChain(http: HttpSecurity, jwtDecoder: JwtDecoder): SecurityFilterChain {
        http
                .authorizeHttpRequests { authorize ->
                    authorize.requestMatchers("/public/**").permitAll()
                            .anyRequest().authenticated()
                }
                .oauth2Login { oauth2Login ->
                    oauth2Login.clientRegistrationRepository(clientRegistrationRepository)
                    oauth2Login.successHandler(CustomAuthenticationSuccessHandler())
                }
                .oauth2ResourceServer { oauth2 ->
                    oauth2.jwt {
                        it.decoder(jwtDecoder)
                    }
                }
                .exceptionHandling { exceptions ->
                    exceptions.authenticationEntryPoint(CustomAuthenticationEntryPoint("http://localhost:8080/auth"))
                }
                .addFilterBefore(tenantIdentificationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }


    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}