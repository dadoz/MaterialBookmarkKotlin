package com.application.material.bookmarkswallet.app.utils

import android.content.Context
import com.application.material.bookmarkswallet.app.R
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone.Companion.UTC
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toJavaZoneId
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import timber.log.Timber
import java.time.Instant
import java.time.LocalDate.now
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant

const val HYPHEN = "-"
const val MIN_SLOT_TIME_MINUTES = 0L
const val ONE_MINUTE_INT = 60
const val PLUS = "+"
const val SPACE = " "
const val THIRTY = 30
const val TIMEZONE_T = "T"
const val TIMEZONE_Z = "Z"
const val TWO_L = 2L
const val ZEROL = 0L

const val FORMAT_DATE = "dd/MM/yyyy"
const val ITALIAN_FORMAT_DATE_CARD = "dd MMMM yyyy"
const val FORMAT_DATE_CARD = "MMMM dd, yyyy"
const val ITALIAN_SHORT_FORMAT_DATE_CARD = "dd MMM yyyy"
const val FORMAT_TIME_DATE = "HH:mm - dd/MM/yyyy"
const val FORMAT_DATE_EXPLICIT_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss[xxx]"
const val FORMAT_DATE_NO_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss"
const val FORMAT_DATE_LITE = "dd MMM - HH:mm"
const val FORMAT_TIME = "HH:mm"
const val FORMAT_TIME_EXTENDED = "HH:mm:ss"
const val FORMAT_PARAMS = "%02d:%02d"

fun Date.convertToLocalDateTime(): java.time.LocalDateTime? = this.toInstant()
    .let {
        java.time.LocalDateTime.ofInstant(it, ZoneId.systemDefault())
    }
//    .toKotlinInstant()
//    .toLocalDateTime(
//        timeZone = currentSystemDefault()
//    )

/**
 * DateUtils to handle date from and to BTP (BE) considering BE is always UTC time (always convert
 * from TimeZone to UTC time
 * //todo issue https://github.vodafone.com/VFGroup-BTP-ITA/mva-wevodafone-app-android/issues/19
 *
 *
 * input from BE with TZ or with BE TZ to Local TZ (+01:00 or +02:00 for Italy)
 */
fun String.parseDateToZonedDateTime(): ZonedDateTime? = try {
    this.takeIf { it.isNotEmpty() }
        ?.let {
            when {
                this.contains(PLUS) || this.contains(TIMEZONE_Z) -> ZonedDateTime.parse(this)

                else -> ZonedDateTime.of(
                    this.parseDateFromInstantToJavaLocalDateTime(),
                    currentSystemDefault().toJavaZoneId()
                )
            }
        }
} catch (e: Exception) {
    Timber.e("[parseDateFromUTC] %s", e.message)
    null
}

fun String.parseDateToZonedDateTimeUTC(): ZonedDateTime? = try {
    this.takeIf { it.isNotEmpty() }
        ?.let {
            when {
                this.contains(PLUS) || this.contains(TIMEZONE_Z) -> ZonedDateTime.parse(this)

                else -> ZonedDateTime.of(
                    this.parseZonedDateTimeToJavaLocalDateTimeUTC(),
                    currentSystemDefault().toJavaZoneId()
                )
            }
        }
} catch (e: Exception) {
    Timber.e("[parseDateFromUTC] %s", e.message)
    null
}

/**
 * format string zoned date time with yyyy-MM-dd
 * */
fun java.time.LocalDateTime?.formatZonedDateTime(context: Context): String? = try {
    DateTimeFormatter.ofPattern(FORMAT_DATE_LITE)
        .let { dateTimeFormatter ->
            this?.format(dateTimeFormatter)
        }
        ?.replace(DASH, context.getString(R.string.at_label))
} catch (e: Exception) {
    Timber.e("[formatZonedDateTime] %s", e.message)
    null
}

/**
 * output to Local time zone with Local format
 **/
@OptIn(ExperimentalTime::class)
fun String.parseZonedDateTimeToJavaLocalDateTimeUTC(): java.time.LocalDateTime? = try {
    this.takeIf { it.isNotEmpty() }
        ?.let {
            LocalDateTime.parse(this)
                .toInstant(UTC)
                .toLocalDateTime(UTC)
                .toJavaLocalDateTime()
        }
} catch (e: Exception) {
    Timber.e("[parseDateFromUTC] %s", e.message)
    null
}

/**
 * output to Local time zone with Local format
 **/
@OptIn(ExperimentalTime::class)
fun String.parseDateFromInstantToJavaLocalDateTime(): java.time.LocalDateTime? = try {
    this.takeIf { it.isNotEmpty() }
        ?.let {
            LocalDateTime.parse(this)
                .toInstant(UTC)
                .toLocalDateTime(currentSystemDefault())
                .toJavaLocalDateTime()
        }
} catch (e: Exception) {
    Timber.e("[parseDateFromUTC] %s", e.message)
    null
}

/**
 * output to Local time zone with Local format
 **/
fun String.parsesAndFormatDateTimeToUTC(): String? = try {
    when {
        this.isEmpty() -> null
        else -> {
            val zonedDateTime = try {
                ZonedDateTime.parse(this)
            } catch (e: DateTimeParseException) {
                e.printStackTrace()
                java.time.LocalDateTime.parse(this).atZone(ZoneOffset.UTC)
            }

            DateTimeFormatter.ISO_INSTANT.format(zonedDateTime)
        }
    }
} catch (e: Exception) {
    Timber.e("[parseDateFromUTC] %s", e.message)
    null
}

/**
 * output to Local time zone with Local format
 * 2 Luglio 2023 for example - IT Format
 */
fun String.formatDateToCurrentTimeZone(): String? = try {
    this.takeIf { it.isNotEmpty() }
        ?.parseDateToZonedDateTime()
        ?.let {
            DateTimeFormatter
                .ofPattern(ITALIAN_FORMAT_DATE_CARD)
                .format(it)
                .let {
                    it.replaceRange(3, 4, it[3].uppercase())
                }
        }
} catch (e: Exception) {
    Timber.e("[formatDateToCurrentTimeZone] %s", e.message)
    null
}

/**
 * output to Local time zone with Local format
 * 2 Luglio 2023 for example - IT Format
 */
fun String.formatDateToCurrentTimeZoneShort(): String? = try {
    this.takeIf { it.isNotEmpty() }?.parseDateToZonedDateTime()?.let {
        DateTimeFormatter.ofPattern(ITALIAN_SHORT_FORMAT_DATE_CARD).format(it).let {
            it.replaceRange(3, 4, it[3].uppercase())
        }
    }
} catch (e: Exception) {
    Timber.e("[formatDateToCurrentTimeZone] " + e.message)
    null
}

/**
 * output to Local time zone with Local format
 * November 20, 2023 for example - IT Format
 */
fun String.formatDateTimeToCurrentTimeZone(): String? = try {
    this.takeIf { it.isNotEmpty() }?.parseDateToZonedDateTime()?.let {
        DateTimeFormatter.ofPattern(FORMAT_DATE_CARD).format(it)
    }
} catch (e: Exception) {
    Timber.e("[formatDateToCurrentTimeZone] " + e.message)
    null
}


/**
 * output to Local time zone with Local format
 */
fun String.formatTimeToCurrentTimeZone(): String? = try {
    this.takeIf { it.isNotEmpty() }
        ?.parseDateToZonedDateTime()
        ?.let {
            DateTimeFormatter
                .ofPattern(FORMAT_TIME)
                .format(it)
        }
} catch (e: Exception) {
    Timber.e("[formatTimeToCurrentTimeZone] %s", e.message)
    null
}

/**
 * output to Local time zone with Local format
 */
fun String.formatDateTimeToTimeUTC(): String? =
    try {
        this.takeIf { it.isNotEmpty() }
            ?.parseDateToZonedDateTimeUTC()
            ?.toLocalDateTime()
            ?.toInstant(ZoneOffset.UTC)
            ?.atZone(ZoneId.systemDefault())
            ?.let {
                DateTimeFormatter
                    .ofPattern(FORMAT_TIME)
                    .format(it)
            }
    } catch (e: Exception) {
        Timber.e("[formatDateTimeToTimeUTC] %s", e.message)
        null
    }

/**
 * output to Local time zone with explicit timezone to be written on DB
 * Find me as collaborator Marco ;) #devJokes
 */
fun String.formatDateToUTCTimeZoneISO8601(): String? = try {
    this.takeIf { it.isNotEmpty() }
        ?.parseDateToZonedDateTime()
        ?.let {
            ZonedDateTime
                .ofInstant(it.toInstant(), ZoneOffset.UTC)
                .format(
                    DateTimeFormatter.ofPattern(FORMAT_DATE_EXPLICIT_TIMEZONE)
                )
        }
} catch (e: Exception) {
    Timber.e("[formatDateToTimeZoneExplicit] %s", e.message)
    null
}

fun String.formatDateToFixedUTCTimeZoneISO8601(): String? = try {
    this.takeIf { it.isNotEmpty() }
        ?.parseDateToZonedDateTimeUTC()
        ?.let {
            ZonedDateTime
                .ofInstant(it.toInstant(), ZoneOffset.UTC)
                .withFixedOffsetZone()
                .format(
                    DateTimeFormatter.ofPattern(FORMAT_DATE_EXPLICIT_TIMEZONE)
                )
        }
} catch (e: Exception) {
    Timber.e("[formatDateToTimeZoneExplicit] %s", e.message)
    null
}

/**
 * output to Local time zone with explicit timezone to be written on DB
 */
fun LocalDateTime.formatDateToUTCTimeZoneISO8601(): String? = try {
    //set time zone
    this.toJavaLocalDateTime()
        .atZone(currentSystemDefault().toJavaZoneId())
        .format(
            DateTimeFormatter.ofPattern(FORMAT_DATE_EXPLICIT_TIMEZONE)
        )
} catch (e: Exception) {
    Timber.e("[formatDateToTimeZoneExplicit] %s", e.message)
    null
}

fun LocalDateTime?.formatDateToUTCISO8601(): String? = try {
    //set time zone
    this?.toJavaLocalDateTime()
        ?.atZone(currentSystemDefault().toJavaZoneId())
        ?.format(
            DateTimeFormatter.ofPattern(FORMAT_DATE_NO_TIMEZONE)
        )
} catch (e: Exception) {
    Timber.e("[formatDateToTimeZoneExplicit] %s", e.message)
    null
}

@OptIn(ExperimentalTime::class)
fun Long?.createLocalDateTime(): LocalDateTime? = this?.let {
    Instant
        .ofEpochMilli(it)
        .toKotlinInstant()
        .toLocalDateTime(currentSystemDefault())
}

fun Long?.createNowDateTimeMorningFormatExplicit(): String? = this?.let {
    createNowDateTimeMorning()
        ?.formatDateToUTCTimeZoneISO8601()
}

@OptIn(ExperimentalTime::class)
fun Long?.createNowDateTimeMorning(): LocalDateTime? = this?.let {
    Instant
        .ofEpochMilli(it)
        .toKotlinInstant()
        .toLocalDateTime(currentSystemDefault())
        .date.atTime(9, 0, 0)
}

fun Long?.createNowDateTimeAfternoonFormatExplicit(): String? = this?.let {
    createNowDateTimeAfternoon()
        ?.formatDateToUTCTimeZoneISO8601()
}

@OptIn(ExperimentalTime::class)
fun Long?.createNowDateTimeAfternoon(): LocalDateTime? = this?.let {
    Instant
        .ofEpochMilli(it)
        .toKotlinInstant()
        .toLocalDateTime(currentSystemDefault())
        .date
        .atTime(
            18, 0, 0
        )
}

fun Long?.toJavaLocalDate(): java.time.LocalDate? = this?.let {
    Instant
        .ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

@OptIn(ExperimentalTime::class)
fun createNowDateTime(): LocalDateTime =
    Clock.System.now()
        .toLocalDateTime(currentSystemDefault())

fun createNowTimeString(): String =
    createNowDateTime()
        .time
        .toJavaLocalTime()
        .format(DateTimeFormatter.ofPattern(FORMAT_TIME))

//todo to change
fun createNowDateTimeMorningUTCString(): String? =
    getTodayMillis()
        .createNowDateTimeMorning()
        ?.formatDateToUTCTimeZoneISO8601()

//todo to change
fun createDateUTCString(instant: Instant): String =
    DateTimeFormatter
        .ofPattern(FORMAT_DATE_EXPLICIT_TIMEZONE)
        .withZone(ZoneId.of("UTC"))
        .format(instant)

@OptIn(ExperimentalTime::class)
fun createNowDateUTCString() =
    createDateUTCString(
        instant = createNowDateTime()
            .toInstant(currentSystemDefault())
            .toJavaInstant()
    )

fun createMonthsAgoStartDateTimeString(
    monthsAgo: Long = TWO_L
): String =
    DateTimeFormatter
        .ofPattern(FORMAT_DATE_EXPLICIT_TIMEZONE)
        .format(
            now()
                .minusMonths(monthsAgo)
                .withDayOfMonth(ONE)
                .atStartOfDay()
                .atOffset(ZoneOffset.UTC)
        )

@OptIn(ExperimentalTime::class)
private fun getTodayMillis(): Long {
    return Clock.System.now().toEpochMilliseconds()
}

@OptIn(ExperimentalTime::class)
fun getTodayInSeconds(): Long =
    Clock.System.now()
        .epochSeconds

fun String.isReservationFuture(): Boolean = isGreaterThanToday()

fun String.isReservationPast(): Boolean = !isGreaterThanToday()

fun String.isGreaterThanToday(): Boolean = try {
    this.parseDateToZonedDateTime()
        ?.let {
            it.toEpochSecond() > getTodayInSeconds()
        } ?: false
} catch (e: Exception) {
    e.printStackTrace()
    false
}

@OptIn(ExperimentalTime::class)
fun java.time.LocalDate.createStartDayDateTime() =
    this.toKotlinLocalDate()
        .atStartOfDayIn(currentSystemDefault())
        .toLocalDateTime(currentSystemDefault())
        .formatDateToUTCTimeZoneISO8601()


@OptIn(ExperimentalTime::class)
private fun getCurrentMinDateInstantEnabled(): kotlinx.datetime.Instant =
    Clock.System.now()
        .minus(DateTimePeriod(days = ONE), currentSystemDefault())

fun java.time.LocalDate.createEndDayDateTime() =
    this.toKotlinLocalDate()
        .atTime(hour = 23, minute = 59, second = 59, nanosecond = 0)
        .formatDateToUTCTimeZoneISO8601()

fun java.time.LocalDate.getMillis() =
    this.atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()


fun String?.startDayMillis(): Long = this?.toWevLocalDateTime()
    ?.toJavaLocalDateTime()
    ?.toLocalDate()
    ?.getCurrentMinDateTimeMillsEnabled() ?: ZEROL

@OptIn(ExperimentalTime::class)
private fun getCurrentMaxDateInstantEnabled(): kotlinx.datetime.Instant =
    getCurrentMinDateInstantEnabled()
        .plus(DateTimePeriod(months = ONE), currentSystemDefault())

@OptIn(ExperimentalTime::class)
fun getCurrentMinDateMillsEnabled() =
    getCurrentMinDateInstantEnabled()
        .toEpochMilliseconds()

@OptIn(ExperimentalTime::class)
fun getCurrentMaxDateMillsEnabled() =
    getCurrentMaxDateInstantEnabled()
        .toEpochMilliseconds()

@OptIn(ExperimentalTime::class)
fun getCurrentMinDateEnabled(): LocalDate =
    getCurrentMinDateInstantEnabled()
        .toLocalDateTime(currentSystemDefault())
        .date

@OptIn(ExperimentalTime::class)
fun getCurrentMaxDateEnabled(): LocalDate =
    getCurrentMaxDateInstantEnabled()
        .toLocalDateTime(currentSystemDefault())
        .date

@OptIn(ExperimentalTime::class)
fun java.time.LocalDate.getCurrentMinDateTimeMillsEnabled(): Long =
    this.atStartOfDay()
        .toKotlinLocalDateTime()
        .toInstant(currentSystemDefault())
        .toEpochMilliseconds()

@OptIn(ExperimentalTime::class)
fun java.time.LocalDate.getEndMinDateTimeMillsEnabled(daysUntil: java.time.LocalDate): Long =
    this.minusDays(daysUntil.toEpochDay())
        .atStartOfDay()
        .toKotlinLocalDateTime()
        .toInstant(currentSystemDefault()).toEpochMilliseconds()

/**
 * we're adding 30 days instead of adding 1 month
 */
@OptIn(ExperimentalTime::class)
fun java.time.LocalDate.getCurrentMaxDateTimeMillsEnabled(days: Int = THIRTY): Long =
    this.plusDays(days.toLong())
        .atStartOfDay()
        .toKotlinLocalDateTime()
        .toInstant(currentSystemDefault())
        .toEpochMilliseconds()


@OptIn(ExperimentalTime::class)
fun String.parseToLocalDate(): LocalDate? = try {
    this
        .takeIf { it.isNotEmpty() }
        ?.let {
            LocalDateTime.parse(this)
                .toInstant(currentSystemDefault())
                .toLocalDateTime(currentSystemDefault())
                .date
        }
} catch (e: Exception) {
    Timber.e("[parseToLocalDate] %s", e.message)
    null
}

fun String.parseToLocalDateString(): String? {
    return try {
        // o la tua timezone desiderata
        when {
            this.isEmpty() -> return null
            else -> {
                val zoneId = ZoneId.of("Europe/Rome") // o la tua timezone desiderata

                val zonedDateTime = try {
                    OffsetDateTime.parse(this).atZoneSameInstant(zoneId)
                } catch (e: DateTimeParseException) {
                    e.printStackTrace()
                    java.time.LocalDateTime.parse(this).atZone(zoneId)
                }

                zonedDateTime.toLocalDate().format(DateTimeFormatter.ofPattern(FORMAT_DATE_LITE))
            }
        }
    } catch (e: Exception) {
        Timber.e("[parseToLocalDate] %s", e.message)
        null
    }
}

fun LocalDate.isGreaterThan(reservedFromDate: LocalDate): Boolean = this > reservedFromDate

fun LocalDate.isLowerThan(reservedToDate: LocalDate): Boolean = this < reservedToDate

fun String.toWevLocalDateTime(): LocalDateTime? = try {
    this.parseDateToZonedDateTime()?.toLocalDateTime()?.toKotlinLocalDateTime()
} catch (e: Exception) {
    e.printStackTrace()
    null
}

/**
 * prev time with 30 min slot time
 */
fun String.prevSlotTime(): String? =
    this.parseSlotTime()
        ?.minusMinutes(MIN_SLOT_TIME_MINUTES)
        ?.format(DateTimeFormatter.ofPattern(FORMAT_TIME_EXTENDED))

/**
 * next time with 30 min slot time
 */
fun String.nextSlotTime(): String? =
    this.parseSlotTime()
        ?.plusMinutes(MIN_SLOT_TIME_MINUTES)
        ?.format(DateTimeFormatter.ofPattern(FORMAT_TIME_EXTENDED))

/**
 * parse time
 */
fun String.parseSlotTime(): OffsetTime? = try {
    DateTimeFormatter
        .ofPattern(FORMAT_TIME_EXTENDED)
        .withZone(ZoneOffset.UTC)
        .let {
            LocalTime
                .parse(this, it)
                .atOffset(ZoneOffset.UTC)
        }
} catch (e: Exception) {
    Timber.e("[parseSlotTime] %s", e.message)
    null
}

/**
 * parse time
 */
fun String.parseAndRetrieveDateBySlotTime(
    slotTime: String?, hasToAdd30Minutes: Boolean = false
): String? = try {
    slotTime
        ?.parseSlotTime()
        ?.plusMinutes(
            when {
                hasToAdd30Minutes -> MIN_SLOT_TIME_MINUTES
                else -> ZEROL
            }
        )
        ?.atDate(
            this.parseDateToZonedDateTime()
                ?.toLocalDate()
        )
        ?.toLocalDateTime()
        ?.toKotlinLocalDateTime()
        ?.formatDateToUTCISO8601()
} catch (e: Exception) {
    Timber.e("[parseAndRetrieveDateBySlotTime] %s", e.message)
    null
}

fun LocalDateTime.toStringFormat(format: String): String? = try {
    val formatter = DateTimeFormatter
        .ofPattern(format)
    this.toJavaLocalDateTime()
        .format(formatter)
} catch (e: Exception) {
    Timber.e("[toStringFormat] %s", e.message)
    null
}

@OptIn(ExperimentalTime::class)
fun LocalDateTime.plusSeconds(seconds: Long): LocalDateTime =
    this.toInstant(currentSystemDefault())
        .plus(seconds, DateTimeUnit.SECOND)
        .toLocalDateTime(currentSystemDefault())

@OptIn(ExperimentalTime::class)
fun LocalDateTime.plusMillis(millis: Long): LocalDateTime =
    this.toInstant(currentSystemDefault())
        .plus(millis, DateTimeUnit.MILLISECOND)
        .toLocalDateTime(currentSystemDefault())

@OptIn(ExperimentalTime::class)
fun Date.toWevLocalDateTime() =
    this.toInstant()
        .toKotlinInstant()
        .toLocalDateTime(currentSystemDefault())

@OptIn(ExperimentalTime::class)
fun String?.parseToLocalDate2() =
    this?.parseToLocalDate()
        ?.atStartOfDayIn(currentSystemDefault())
        ?.toEpochMilliseconds()

fun String.splitTimeSlots(
    endTime: String?,
    minutesInterval: Long = THIRTY.toLong()
): List<Pair<String, String>> {
    try {
        val formatter = DateTimeFormatter.ofPattern(FORMAT_TIME_EXTENDED)
        var startTimeSlot = LocalTime.parse(this, formatter)
        val endTimeSlot = LocalTime.parse(endTime, formatter)
        val timeSlots = mutableListOf<Pair<String, String>>()

        while (startTimeSlot.isBefore(endTimeSlot)) {
            val slotEndTime = startTimeSlot.plusMinutes(minutesInterval)
            if (slotEndTime.isAfter(endTimeSlot)) break

            timeSlots.add(Pair(formatter.format(startTimeSlot), formatter.format(slotEndTime)))

            startTimeSlot = slotEndTime
        }
        return timeSlots
    } catch (e: Exception) {
        Timber.e("[splitTimeSlots] %s", e.message)
        return emptyList()
    }
}

fun String.replaceTimeSlot(time: String): String? = LocalTime.parse(time)
    .let { localTime ->
        LocalDateTime.parse(this)
            .date
            .atTime(
                localTime.hour, localTime.minute, localTime.second
            )
            .formatDateToUTCISO8601()
    }

fun String?.parseToEpochMilli() =
    this?.parseDateToZonedDateTime()
        ?.toLocalDateTime()
        ?.toInstant(ZoneOffset.UTC)
        ?.toEpochMilli()

fun String?.getUTCTime(): String? =
    this?.parseDateToZonedDateTimeUTC()
        ?.let { zonedDateTime ->
            DateTimeFormatter
                .ofPattern(FORMAT_TIME_EXTENDED)
                .format(zonedDateTime)
        }

fun areDatesInDatesRange(
    startDate: String?,
    endDate: String?,
    rangeStartDate: String?,
    rangeEndDate: String?
): Boolean = try {
    startDate.compareGreaterThanLocalDateTime(other = rangeStartDate)
            && endDate.compareLessThanLocalDateTime(other = rangeEndDate)
} catch (e: Exception) {
    Timber.e("[areDatesInDatesRange] %s", e.message)
    false
}

fun String?.createNotificationTimestamp(atString: String): String =
    this?.formatDateToCurrentTimeZone()
        ?.let {
            "${it}$SPACE"
                .plus(atString)
                .plus("$SPACE${this.formatDateTimeToTimeUTC()}")
        } ?: HYPHEN

fun LocalDateTime.isSameDayOf(localDateTime: LocalDateTime) =
    this.dayOfYear == localDateTime.dayOfYear && this.year == localDateTime.year


fun formatTime(seconds: Int): String {
    val minutes = seconds / ONE_MINUTE_INT
    val remainingSeconds = seconds % ONE_MINUTE_INT
    return FORMAT_PARAMS.format(minutes, remainingSeconds)
}

fun String?.compareGreaterThanLocalDateTime(other: String?): Boolean = try {
    this
        ?.let { actual ->
            other
                ?.let {
                    LocalDateTime.parse(actual) >= LocalDateTime.parse(other)
                }
        } ?: false
} catch (e: Exception) {
    Timber.e("[compareGreaterThanLocalDateTime] %s", e.message)
    false
}

fun String?.compareLessThanLocalDateTime(other: String?): Boolean = try {
    this
        ?.let { actual ->
            other
                ?.let {
                    LocalDateTime.parse(actual) <= LocalDateTime.parse(other)
                }
        } ?: false
} catch (e: Exception) {
    Timber.e("[compareLessThanLocalDateTime] %s", e.message)
    false
}

//is Token expired
fun LocalDateTime?.isExpired() = try {
    this
        ?.let { expirationDate ->
            createNowDateTime().toJavaLocalDateTime()
                .isAfter(expirationDate.toJavaLocalDateTime())
        } ?: true
} catch (e: Exception) {
    Timber.e("[isTokenExpired] %s", e.message)
    false
}

fun Int.isHourLimitExpired(dateString: String? = null): Boolean {
    return try {
        val targetDateTime = try {
            OffsetDateTime.parse(dateString)
        } catch (e: Exception) {
            e.printStackTrace()
            java.time.LocalDateTime.parse(dateString).atZone(ZoneId.systemDefault())
                .toOffsetDateTime()
        }

        val now = OffsetDateTime.now()
        val today = now.toLocalDate()
        val limitTimeToday = LocalTime.of(this, 0)

        if (targetDateTime.toLocalDate() == today.plusDays(1)) {
            now.toLocalTime().isAfter(limitTimeToday)
        } else {
            false
        }
    } catch (e: Exception) {
        false
    }
}

fun String?.isToday(): Boolean {
    return try {
        this.parseDateLocal()
            .isEqual(now())
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

@Throws(DateTimeParseException::class)
fun String?.parseDateLocal(): java.time.LocalDate = try {
    OffsetDateTime.parse(this)
        .toLocalDate()
} catch (e: Exception) {
    e.printStackTrace()
    java.time.LocalDateTime.parse(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

@Throws(DateTimeParseException::class)
fun String?.parseDateLocalToString(): String? = try {
    java.time.LocalDateTime.parse(this)
        .toLocalDate()
        .format(DateTimeFormatter.ISO_LOCAL_DATE)
} catch (e2: Exception) {
    e2.printStackTrace()
    null
}

fun String.extractDate(): String? {
    return try {
        val parsedDate = OffsetDateTime.parse(this).toLocalDate()
        parsedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
    } catch (e: Exception) {
        e.printStackTrace()
        this.parseDateLocalToString()
    }
}
