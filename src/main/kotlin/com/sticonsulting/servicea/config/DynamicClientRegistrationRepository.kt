package com.sticonsulting.servicea.config

import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class DynamicClientRegistrationRepository(private var adminService: AdminService) : ClientRegistrationRepository {
    override fun findByRegistrationId(registrationId: String?): ClientRegistration {
        return adminService.fetchClientConfigForTenant(registrationId!!)
    }


}
