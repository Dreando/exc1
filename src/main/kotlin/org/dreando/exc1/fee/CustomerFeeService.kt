package org.dreando.exc1.fee

import org.dreando.exc1.csv.CsvReader
import org.dreando.exc1.feereport.FeeReportService
import org.dreando.exc1.transaction.TransactionService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.publisher.ParallelFlux
import reactor.core.scheduler.Schedulers
import java.util.*


@Service
class CustomerFeeService(
    csvReader: CsvReader,
    @Value("\${data.csv.fee-wages-location}") private val feeWagesFileLocation: String,
    private val transactionService: TransactionService,
    private val feeReportService: FeeReportService
) {

    private val logger = LoggerFactory.getLogger(CustomerFeeService::class.java)

    private val availableProcessors = Runtime.getRuntime().availableProcessors()
    private val ioScheduler = Schedulers.newBoundedElastic(availableProcessors, Int.MAX_VALUE, "new-io-scheduler")
    private val parallelScheduler = Schedulers.newParallel("new-parallel-scheduler", availableProcessors)

    private val wages = csvReader.readFeeWages(feeWagesFileLocation)
        .associateByTo(TreeMap()) { it.transactionsValueUpperBound }
    private val wageUpperRanges = wages.navigableKeySet()

    // TODO: some logs would be appreciated
    fun calculateCustomersFee(customerIds: List<Int>): ParallelFlux<CustomerFee> {
        return transactionService.getTransactions(customerIds)
            .parallel()
            .runOn(parallelScheduler)
            .map { customerTransactions -> customerTransactions.sortedBy { tx -> tx.transactionDate } }
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
            }.doOnNext { fee ->
                // Fire & forget
                Mono.fromRunnable<Unit> { feeReportService.reportFeeCalculation(fee.customerId, fee.fee).subscribe() }
                    .subscribeOn(ioScheduler)
                    .subscribe()
            }
    }

    private fun findFeeWageFor(amount: Double): Double {
        for (upperRange in wageUpperRanges) {
            if (amount <= upperRange) return wages[upperRange]?.transactionFeeValue ?: 0.0
        }
        return wages[wageUpperRanges.last()]?.transactionFeeValue ?: 0.0
    }
}