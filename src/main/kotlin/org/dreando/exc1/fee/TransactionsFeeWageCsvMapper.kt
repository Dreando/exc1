package org.dreando.exc1.fee

import org.dreando.exc1.csv.getOrThrow
import org.dreando.exc1.csv.toDouble

private const val TRANSACTIONS_VALUE_UPPER_BOUND_HEADER = "transaction_value_less_than"
private const val TRANSACTION_FEE_VALUE = "fee_percentage_of_transaction_value"

fun mapRowToTransactionsFeeWage(row: Map<String, String>): TransactionsFeeWage {
    return TransactionsFeeWage(
        transactionsValueUpperBound = row.getOrThrow(TRANSACTIONS_VALUE_UPPER_BOUND_HEADER).toInt(),
        transactionFeeValue = row.getOrThrow(TRANSACTION_FEE_VALUE).toDouble()
    )
}
