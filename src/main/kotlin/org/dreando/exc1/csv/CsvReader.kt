package org.dreando.exc1.csv

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.springframework.stereotype.Component
import java.io.File

@Component
class CsvReader {

    fun <T> readRowsToModel(csvFileLocation: String, rowMapper: (Map<String, String>) -> T): List<T> {
        return csvReader().readAllWithHeader(File(csvFileLocation)).let { rows ->
            rows.mapNotNull { row ->
                try {
                    rowMapper(row)
                } catch (exception: Exception) {
                    null
                }
            }
        }
    }
}
