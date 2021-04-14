package org.dreando.exc1.api

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