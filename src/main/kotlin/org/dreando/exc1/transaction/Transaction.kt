package org.dreando.exc1.transaction

import java.time.LocalDateTime

data class Transaction(
        val transactionId: Int,
        val transactionAmount: Double,
        val customerId: Int,
        val customerFirstName: String,
        val customerLastName: String,
        val transactionDate: LocalDateTime
)
