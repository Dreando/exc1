package org.dreando.exc1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import javax.annotation.PostConstruct

const val COMMON_DATE_PATTERN = "dd.MM.yyyy HH:mm:ss"

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@PostConstruct
fun started() {
    TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC))
}
