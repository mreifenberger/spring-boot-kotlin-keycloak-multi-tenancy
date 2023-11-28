package com.sticonsulting.servicea.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder

@Configuration
class JwtDecoderConfig {

    @Bean
    fun jwtDecoder(requestScopedBean: RequestScopedBean): JwtDecoder {
        return JwtDecoder { jwtToken ->
            val realm = TenantContext.getCurrentTenant()
            val jwkSetUri = getJwkSetUriForRealm(realm)
            NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build().decode(jwtToken)
        }
    }

    private fun getJwkSetUriForRealm(realm: String): String {
        // Logic to retrieve the JWK Set URI for the specified realm
        return "http://localhost:8080/auth/realms/$realm/protocol/openid-connect/certs"
    }
}

