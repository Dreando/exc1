package org.dreando.exc1.user

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "security")
data class UserCredentialsProvider(
        val predefinedUserCredentials: Map<String, String>
)