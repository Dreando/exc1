package org.dreando.exc1.transaction

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class TransactionService(
    csvReader: CsvReader,
    @Value("\${data.csv.transactions-location}") private val transactionsFileLocation: String
) {

    private val customerTransactions = csvReader.readTransactions(transactionsFileLocation).groupBy {
        it.customerId
    }

    fun getCustomerTransactions(customerId: Int) =
        customerTransactions[customerId]?.let { transactions -> Flux.fromIterable(transactions) }
            ?: throw NoCustomerFound(customerId)
}