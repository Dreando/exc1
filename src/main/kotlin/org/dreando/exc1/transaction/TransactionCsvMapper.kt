package org.dreando.exc1.transaction

import org.dreando.exc1.COMMON_DATE_PATTERN
import org.dreando.exc1.csv.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TRANSACTION_ID_HEADER = "transaction_id"
private const val TRANSACTION_AMOUNT_HEADER = "transaction_amount"
private const val CUSTOMER_ID_HEADER = "customer_id"
private const val CUSTOMER_FIRST_NAME_HEADER = "customer_first_name"
private const val CUSTOMER_LAST_NAME_HEADER = "customer_last_name"
private const val TRANSACTION_DATE_HEADER = "transaction_date"

private val dateTimeFormatter = DateTimeFormatter.ofPattern(COMMON_DATE_PATTERN)

fun mapRowToTransaction(row: Map<String, String>): Transaction {
    return Transaction(
        transactionId = row.getOrThrow(TRANSACTION_ID_HEADER).toInt(),
        transactionAmount = row.getOrThrow(TRANSACTION_AMOUNT_HEADER).toDouble(),
        customerId = row.getOrThrow(CUSTOMER_ID_HEADER).toInt(),
        customerFirstName = row.getOrThrow(CUSTOMER_FIRST_NAME_HEADER),
        customerLastName = row.getOrThrow(CUSTOMER_LAST_NAME_HEADER),
        transactionDate = LocalDateTime.parse(row.getOrThrow(TRANSACTION_DATE_HEADER), dateTimeFormatter)
    )
}
