package org.dreando.exc1.csv

import java.text.NumberFormat
import java.util.*

fun Map<String, String>.getOrThrow(
    key: String,
    exception: RuntimeException = RuntimeException("Value $key is missing in this row $this")
) = this[key] ?: throw exception

fun String.toDouble(numberFormat: NumberFormat = NumberFormat.getInstance(Locale.getDefault())): Double {
    return numberFormat.parse(this).toDouble()
}