package org.dreando.exc1.feereport

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FeeReportRepository(private val mongoTemplate: ReactiveMongoTemplate) {

    fun save(feeReport: FeeReport): Mono<FeeReport> {
        return mongoTemplate.save(feeReport)
    }
}
