package org.dreando.exc1.api

import com.fasterxml.jackson.annotation.JsonFormat
import org.apache.commons.math3.util.Precision.round
import org.dreando.exc1.COMMON_DATE_PATTERN
import org.dreando.exc1.fee.CustomerFee
import java.time.LocalDateTime

data class CustomerFeeResponse(
    val customerId: Int,
    val customerFirstName: String,
    val customerLastName: String,
    val lastMonthTransactionsNumber: Int,
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT, pattern = "%.2")
    val lastMonthTransactionsValue: Double,
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT, pattern = "%.2")
    val fee: Double,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = COMMON_DATE_PATTERN)
    val lastTransactionDateTime: LocalDateTime
)

fun CustomerFee.toResponse(): CustomerFeeResponse {
    return CustomerFeeResponse(
        customerId = customerId,
        customerFirstName = customerFirstName,
        customerLastName = customerLastName,
        lastMonthTransactionsNumber = lastMonthTransactionsNumber,
        lastMonthTransactionsValue = round(lastMonthTransactionsValue, 2),
        fee = round(fee, 2),
        lastTransactionDateTime = lastTransactionDateTime
    )
}