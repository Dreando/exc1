package org.dreando.exc1.transaction

import org.dreando.exc1.csv.CsvReader
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class TransactionService(
    csvReader: CsvReader,
    @Value("\${data.csv.transactions-location}") private val transactionsFileLocation: String
) {

    // TODO: some logs would be appreciated
    private val transactions = csvReader.readTransactions(transactionsFileLocation).groupBy {
        it.customerId
    }

    // TODO: describe behaviour (why empty and not errorstate/exception)
    fun getTransactions(customerIds: List<Int>): Flux<Transaction> {
        return if (customerIds.isEmpty()) {
            Flux.fromIterable(transactions.values.flatten())
        } else Flux.fromIterable(customerIds.flatMap { transactions[it] ?: listOf() })
    }
}