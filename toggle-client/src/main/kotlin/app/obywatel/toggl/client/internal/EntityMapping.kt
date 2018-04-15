package app.obywatel.toggl.client.internal

import app.obywatel.toggl.client.entity.*
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

private fun String.toTimestamp() = ZonedDateTime.parse(this, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toEpochSecond()

private fun String.fromHexColorToInt(): Int {

    val colorStr = if (this.startsWith("#")) this.substring(1) else this
    var color = colorStr.toLongOrNull(16) ?: 0

    if (colorStr.length == 6) {
        color = color or 0x00000000ff000000
    }
    return color.toInt()
}

internal fun app.obywatel.toggl.client.internal.entity.User.toExternal() =
    User(
        id = id,
        apiToken = api_token,
        defaultWorkspaceId = default_wid,
        fullName = fullname,
        email = email,
        beginningOfWeek = Day.fromValue(beginning_of_week),
        language = language,
        timezone = timezone,
        imageUrl = image_url ?: "",
        creationTimestamp = created_at.toTimestamp(),
        lastUpdateTimestamp = at.toTimestamp()
    )

internal fun app.obywatel.toggl.client.internal.entity.Workspace.toExternal() =
    Workspace(
        id = id,
        name = name,
        admin = admin,
        defaultCurrency = default_currency,
        logoUrl = logo_url ?: "",
        premium = premium,
        rounding = RoundingType.fromValue(rounding),
        roundingMinutes = rounding_minutes,
        onlyAdminsMayCreateProjects = only_admins_may_create_projects,
        onlyAdminsSeeBillableRates = only_admins_see_billable_rates,
        defaultHourlyRate = default_hourly_rate ?: 0.0,
        lastUpdateTimestamp = at.toTimestamp()
    )

internal fun app.obywatel.toggl.client.internal.entity.Project.toExternal() =
    Project(
        id = id,
        name = name,
        workspaceId = wid,
        clientId = cid,
        active = active,
        private = is_private,
        creationTimestamp = at.toTimestamp(),
        colorId = color,
        color = hex_color.fromHexColorToInt()
    )