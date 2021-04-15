package org.dreando.exc1.feereport

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("fee-report")
data class FeeReport(
    @Id val id: ObjectId = ObjectId(),
    val customerId: Int,
    val reportedFee: Double,
    val reportCreationDate: LocalDateTime
)