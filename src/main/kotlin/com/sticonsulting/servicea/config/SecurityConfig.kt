package com.sticonsulting.servicea.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.client.RestTemplate

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Autowired
    private lateinit var tenantIdentificationFilter: TenantIdentificationFilter


    @Bean
    fun securityFilterChain(http: HttpSecurity, jwtDecoder: JwtDecoder): SecurityFilterChain {
        http
                .authorizeHttpRequests { authorize ->
                    authorize.requestMatchers("/public/**").permitAll()
                            .anyRequest().authenticated()
                }
                .oauth2Login { oauth2Login ->
                    oauth2Login.defaultSuccessUrl("http://localhost:8081/api/hello")
                    /*oauth2Login.userInfoEndpoint {
                        userinfoendpoint ->
                        userinfoendpoint.oidcUserService(oidcUserService())

                    }*/
                }
                .oauth2ResourceServer { oauth2 ->
                    oauth2.jwt {
                        it.decoder(jwtDecoder)
                    }
                }
                .addFilterBefore(tenantIdentificationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }


    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean
    fun oidcUserService(): OAuth2UserService<OidcUserRequest, OidcUser> {
        val oidcUserService = OidcUserService()
        return OAuth2UserService { userRequest ->
            oidcUserService.loadUser(userRequest)
        }
    }
}