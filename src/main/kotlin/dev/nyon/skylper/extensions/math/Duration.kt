package dev.nyon.skylper.extensions.math

import kotlin.time.Duration

fun Duration.toPrettyString(): String = when (inWholeMilliseconds) {
    0L -> "0s"
    else -> {
        val isNegative = isNegative()
        buildString {
            if (isNegative) append('-')
            absoluteValue.toComponents { days, hours, minutes, seconds, nanoseconds ->
                val hasDays = days != 0L
                val hasHours = hours != 0
                val hasMinutes = minutes != 0
                val hasSeconds = seconds != 0 || nanoseconds != 0
                var components = 0
                if (hasDays) {
                    append(days).append('d')
                    components++
                }
                if (hasHours || (hasDays && (hasMinutes || hasSeconds))) {
                    if (components++ > 0) append(' ')
                    append(hours).append('h')
                }
                if (hasMinutes || (hasSeconds && (hasHours || hasDays))) {
                    if (components++ > 0) append(' ')
                    append(minutes).append('m')
                }
                if (hasSeconds) {
                    if (components++ > 0) append(' ')
                    when {
                        seconds != 0 || hasDays || hasHours || hasMinutes -> appendFractional(seconds, "s")
                        nanoseconds >= 1_000_000 -> appendFractional(nanoseconds / 1_000_000, "ms")
                        nanoseconds >= 1_000 -> appendFractional(nanoseconds / 1_000, "us")
                        else -> append(nanoseconds).append("ns")
                    }
                }
                if (isNegative && components > 1) insert(1, '(').append(')')
            }
        }
    }
}

private fun StringBuilder.appendFractional(whole: Int, unit: String) {
    append(whole)
    append(unit)
}
