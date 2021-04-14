package org.dreando.exc1.transaction

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class NoCustomerFound(customerId: Int) :
    ResponseStatusException(HttpStatus.NOT_FOUND, "No customer of id $customerId found")