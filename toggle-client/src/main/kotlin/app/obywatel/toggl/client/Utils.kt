package app.obywatel.toggl.client

import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

open class EnumCompanion<in T, out V>(private val valueMap: Map<T, V>) {

    fun fromValue(type: T) = valueMap[type] ?: throw IllegalArgumentException()
}

internal fun String.fromHexColorToInt(): Int {

    val colorStr = if (this.startsWith("#")) this.substring(1) else this
    var color = colorStr.toLongOrNull(16) ?: 0

    if (colorStr.length == 6) {
        color = color or 0x00000000ff000000
    }
    return color.toInt()
}

internal fun String.toOffsetDateTime() = OffsetDateTime.parse(this, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
internal fun String.toEpochSecond() = this.toOffsetDateTime().toEpochSecond()

internal fun Long.secondsToOffsetDateTime() = OffsetDateTime.ofInstant(Instant.ofEpochSecond(this), ZoneId.systemDefault())
internal fun Long.secondsToOffsetDateTimeStr() = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(this.secondsToOffsetDateTime())
internal fun Long.secondsToLocalDate() = this.secondsToOffsetDateTime().toLocalDate()
internal fun Long.secondsToLocalDateStr() =  DateTimeFormatter.ISO_LOCAL_DATE.format(this.secondsToLocalDate())

internal fun Long.millisToOffsetDateTime() = OffsetDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
internal fun Long.millisToOffsetDateTimeStr() = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(this.millisToOffsetDateTime())
internal fun Long.millisToLocalDate() = this.millisToOffsetDateTime().toLocalDate()
internal fun Long.millisToLocalDateStr() = DateTimeFormatter.ISO_LOCAL_DATE.format(this.millisToLocalDate())