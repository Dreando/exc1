package org.dreando.exc1.user

import org.springframework.stereotype.Service

@Service
class UserService(private val userCredentialsProvider: UserCredentialsProvider) {

    fun getAllUsers() = userCredentialsProvider.predefinedUserCredentials.map { usernamePasswordEntry ->
        User(usernamePasswordEntry.key, usernamePasswordEntry.value)
    }
}