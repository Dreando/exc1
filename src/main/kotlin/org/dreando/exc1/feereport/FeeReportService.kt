package org.dreando.exc1.feereport

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class FeeReportService(private val feeReportRepository: FeeReportRepository) {

    fun reportFeeCalculation(customerId: Int, reportedFee: Double): Mono<FeeReport> {
        return feeReportRepository.save(
            FeeReport(
                customerId = customerId,
                reportedFee = reportedFee,
                reportCreationDate = LocalDateTime.now()
            )
        )
    }
}
