package org.dreando.exc1.api

import org.dreando.exc1.fee.CustomerFeeService
import org.dreando.exc1.feereport.FeeReportService
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
        ) customerIds: List<String> = listOf()
    ): Flux<CustomerFeeResponse> {
        return Flux.from(
            customerFeeService.calculateCustomersFee(
                customerIds.mapNotNull { if (it == "ALL") null else it.toIntOrNull() }
            ).map { it.toResponse() }
        )
    }
}
