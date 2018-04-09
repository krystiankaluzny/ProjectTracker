package app.obywatel.toggl.client.entity

import app.obywatel.toggl.client.EnumCompanion
import org.threeten.bp.ZonedDateTime

data class Workspace(
    val id: Long,
    val name: String,
    val premium: Boolean,
    val admin: Boolean,
    val defaultHourlyRate: Double,
    val defaultCurrency: String,
    val onlyAdminsMayCreateProjects: Boolean,
    val onlyAdminsSeeBillableRates: Boolean,
    val rounding: RoundingType,
    val roundingMinutes: Int,
    val lastUpdateDate: ZonedDateTime,
    val logoUrl: String
)

enum class RoundingType(val value: Byte) {
    ROUND_DOWN(-1),
    NEAREST(0),
    ROUND_UP(1);

    companion object : EnumCompanion<Byte, RoundingType>(RoundingType.values().associateBy(RoundingType::value))
}