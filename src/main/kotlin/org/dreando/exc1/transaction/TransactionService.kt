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

    private val customerTransactions = csvReader.readRowsToModel(transactionsFileLocation, ::mapRowToTransaction)
        .groupBy { tx -> tx.customerId }

    init {
        logger.info("Loaded transactions of ${customerTransactions.size} customers into memory")
    }

    /**
     * @return emits collections of customers transactions. Returns all customers transactions if empty list provided
     * on input.
     */
    fun getTransactions(customerIds: List<Int>): Flux<List<Transaction>> {
        return if (customerIds.isEmpty()) {
            Flux.fromIterable(customerTransactions.values)
        } else Flux.fromIterable(customerIds.map { customerTransactions[it] ?: listOf() })
    }
}
