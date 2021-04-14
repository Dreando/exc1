package org.dreando.exc1.api

import org.dreando.exc1.transaction.CustomerFee
import java.time.LocalDateTime

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