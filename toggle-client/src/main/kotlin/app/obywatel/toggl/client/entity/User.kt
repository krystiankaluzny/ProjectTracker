package app.obywatel.toggl.client.entity

import org.threeten.bp.ZonedDateTime

data class User(
    val id: Long,
    val apiToken: String,
    val defaultWorkspaceId: Long,
    val fullName: String,
    val email: String,
    val beginningOfWeek: Day,
    val language: String,
    val imageUrl: String,
    val lastUpdateDate: ZonedDateTime,
    val creationDate: ZonedDateTime,
    val timezone: String
)