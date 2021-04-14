package org.dreando.exc1.api

import org.dreando.exc1.fee.CustomerFee
import java.time.LocalDateTime

// TODO: format date, round doubles
data class CustomerFeeResponse(
    val customerId: Int,
    val customerFirstName: String,
    val customerLastName: String,
    val lastMonthTransactionsNumber: Int,
    val lastMonthTransactionsValue: Double,
    val fee: Double,
    val lastTransactionDateTime: LocalDateTime
)

fun CustomerFee.toResponse(): CustomerFeeResponse {
    return CustomerFeeResponse(
        customerId = customerId,
        customerFirstName = customerFirstName,
        customerLastName = customerLastName,
        lastMonthTransactionsNumber = lastMonthTransactionsNumber,
        lastMonthTransactionsValue = lastMonthTransactionsValue,
        fee = fee,
        lastTransactionDateTime = lastTransactionDateTime
    )
}