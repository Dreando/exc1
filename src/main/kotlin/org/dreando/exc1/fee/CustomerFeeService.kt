package org.dreando.exc1.fee

import org.dreando.exc1.csv.CsvReader
import org.dreando.exc1.transaction.Transaction
import org.dreando.exc1.transaction.TransactionService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.util.*

@Service
class CustomerFeeService(
    csvReader: CsvReader,
    @Value("\${data.csv.fee-wages-location}") private val feeWagesFileLocation: String,
    private val transactionService: TransactionService
) {
    private val wages = csvReader.readFeeWages(feeWagesFileLocation)
        .associateByTo(TreeMap()) { it.transactionsValueUpperBound }

    private val wageUpperRanges = wages.navigableKeySet()

    // TODO: some logs would be appreciated
    fun calculateCustomersFee(customerIds: List<Int>): Flux<CustomerFee> {
        return transactionService.getTransactions(customerIds)
            .groupBy(Transaction::customerId)
            .flatMap { customerTransactions -> customerTransactions.collectSortedList(compareBy { tx -> tx.transactionDate }) }
            .map { customerTransactions ->
                val lastTransaction = customerTransactions.last()
                val lastMonthTransactionsValue = customerTransactions.sumByDouble { it.transactionAmount }
                CustomerFee(
                    customerId = lastTransaction.customerId,
                    customerFirstName = lastTransaction.customerFirstName,
                    customerLastName = lastTransaction.customerLastName,
                    lastMonthTransactionsNumber = customerTransactions.size,
                    lastMonthTransactionsValue = lastMonthTransactionsValue,
                    fee = lastMonthTransactionsValue * (findFeeWageFor(lastMonthTransactionsValue) / 100),
                    lastTransactionDateTime = lastTransaction.transactionDate
                )
            }
    }

    private fun findFeeWageFor(amount: Double): Double {
        for (upperRange in wageUpperRanges) {
            if (amount <= upperRange) return wages[upperRange]?.transactionFeeValue ?: 0.0
        }
        return wages[wageUpperRanges.last()]?.transactionFeeValue ?: 0.0
    }
}