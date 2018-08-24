package app.obywatel.toggl.client.entity

import app.obywatel.toggl.client.EnumCompanion

data class User(
    val id: Long,
    val apiToken: String,
    val defaultWorkspaceId: Long,
    val fullName: String,
    val email: String,
    val beginningOfWeek: Day,
    val language: String,
    val imageUrl: String,
    val lastUpdateTimestamp: Long,
    val creationTimestamp: Long,
    val timezone: String
)

enum class Day(val value: Byte) {
    SUNDAY(0),
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6);

    companion object : EnumCompanion<Byte, Day>(Day.values().associateBy(Day::value))
}