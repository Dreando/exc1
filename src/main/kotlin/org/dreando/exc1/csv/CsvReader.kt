package org.dreando.exc1.csv

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.dreando.exc1.COMMON_DATE_PATTERN
import org.dreando.exc1.fee.TransactionsFeeWage
import org.dreando.exc1.transaction.Transaction
import org.springframework.stereotype.Component
import java.io.File
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private const val TRANSACTION_ID_HEADER = "transaction_id"
private const val TRANSACTION_AMOUNT_HEADER = "transaction_amount"
private const val CUSTOMER_ID_HEADER = "customer_id"
private const val CUSTOMER_FIRST_NAME_HEADER = "customer_first_name"
private const val CUSTOMER_LAST_NAME_HEADER = "customer_last_name"
private const val TRANSACTION_DATE_HEADER = "transaction_date"

private const val TRANSACTIONS_VALUE_UPPER_BOUND_HEADER = "transaction_value_less_than"
private const val TRANSACTION_FEE_VALUE = "fee_percentage_of_transaction_value"

// TODO: having tx/wage types here smells a bit...
@Component
class CsvReader {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern(COMMON_DATE_PATTERN)
    private val numberFormat = NumberFormat.getInstance(Locale.getDefault())
    private fun String.toLocalizedDouble(): Double {
        return numberFormat.parse(this).toDouble()
    }

    private fun Map<String, String>.getExceptionally(key: String): String {
        return this[key] ?: throw RuntimeException("Value $key is missing in this row $this")
    }

    fun readTransactions(csvFileLocation: String): List<Transaction> {
        return csvReader().readAllWithHeader(File(csvFileLocation)).let {
            it.mapNotNull { row ->
                try {
                    Transaction(
                        transactionId = row.getExceptionally(TRANSACTION_ID_HEADER).toInt(),
                        transactionAmount = row.getExceptionally(TRANSACTION_AMOUNT_HEADER).toLocalizedDouble(),
                        customerId = row.getExceptionally(CUSTOMER_ID_HEADER).toInt(),
                        customerFirstName = row.getExceptionally(CUSTOMER_FIRST_NAME_HEADER),
                        customerLastName = row.getExceptionally(CUSTOMER_LAST_NAME_HEADER),
                        transactionDate = LocalDateTime.parse(
                            row.getExceptionally(TRANSACTION_DATE_HEADER),
                            dateTimeFormatter
                        )
                    )
                } catch (exception: Exception) {
                    null
                }
            }
        }
    }

    fun readFeeWages(csvFileLocation: String): List<TransactionsFeeWage> {
        return csvReader().readAllWithHeader(File(csvFileLocation)).let {
            it.mapNotNull { row ->
                try {
                    TransactionsFeeWage(
                        transactionsValueUpperBound = row.getExceptionally(TRANSACTIONS_VALUE_UPPER_BOUND_HEADER)
                            .toInt(),
                        transactionFeeValue = row.getExceptionally(TRANSACTION_FEE_VALUE).toLocalizedDouble()
                    )
                } catch (exception: Exception) {
                    null
                }
            }
        }
    }
}
