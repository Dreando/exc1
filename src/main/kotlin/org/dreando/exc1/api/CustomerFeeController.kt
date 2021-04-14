package org.dreando.exc1.api

import org.dreando.exc1.transaction.CustomerFeeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping(CUSTOMER_FEE_ENDPOINT)
class CustomerFeeController(private val customerFeeService: CustomerFeeService) {

    @GetMapping
    fun getLastMonthCustomerFee(
        @RequestParam(
            CUSTOMER_ID_QUERY_PARAM,
            required = false,
            defaultValue = "ALL"
        ) customerId: List<String> = listOf()
    ): Flux<CustomerFeeResponse> {
        println()
        return Flux.from(customerFeeService.calculateCustomerFee(1).map { it.toResponse() })
    }
}