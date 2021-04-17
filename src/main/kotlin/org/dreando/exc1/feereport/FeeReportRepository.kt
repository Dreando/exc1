package org.dreando.exc1.feereport

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Component

@Component
class FeeReportRepository(private val mongoTemplate: ReactiveMongoTemplate) {

    fun save(feeReport: FeeReport) = mongoTemplate.save(feeReport)
}
