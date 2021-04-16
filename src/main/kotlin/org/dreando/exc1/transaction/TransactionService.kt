package org.dreando.exc1.transaction

import org.dreando.exc1.csv.CsvReader
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class TransactionService(
    csvReader: CsvReader,
    @Value("\${data.csv.transactions-location}") private val transactionsFileLocation: String
) {

    private val logger = LoggerFactory.getLogger(TransactionService::class.java)

    private val transactions = csvReader.readRowsToModel(transactionsFileLocation, ::mapRowToTransaction).groupBy {
        it.customerId
    }

    init {
        logger.info("Loaded transactions of ${transactions.size} customers into memory")
    }

    fun getTransactions(customerIds: List<Int>): Flux<List<Transaction>> {
        return if (customerIds.isEmpty()) {
            Flux.fromIterable(transactions.values)
        } else Flux.fromIterable(customerIds.map { transactions[it] ?: listOf() })
    }
}
