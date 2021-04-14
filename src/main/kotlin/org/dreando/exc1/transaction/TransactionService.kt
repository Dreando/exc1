package org.dreando.exc1.transaction

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class TransactionService(
    private val csvReader: CsvReader,
    @Value("\${data.csv.transactions-location}") private val transactionsFileLocation: String
) {

}