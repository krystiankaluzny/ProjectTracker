package app.obywatel.toggl.client.internal

import app.obywatel.toggl.client.entity.*
import app.obywatel.toggl.client.fromHexColorToInt
import app.obywatel.toggl.client.toEpochSecond


internal fun app.obywatel.toggl.client.internal.retrofit.dto.User.expose() =
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
        creationTimestamp = created_at.toEpochSecond(),
        lastUpdateTimestamp = at.toEpochSecond()
    )

internal fun app.obywatel.toggl.client.internal.retrofit.dto.Workspace.expose() =
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
        lastUpdateTimestamp = at.toEpochSecond()
    )

internal fun app.obywatel.toggl.client.internal.retrofit.dto.Project.expose() =
    Project(
        id = id,
        name = name,
        workspaceId = wid,
        clientId = cid,
        active = active,
        private = is_private,
        creationTimestamp = at.toEpochSecond(),
        colorId = color,
        color = hex_color.fromHexColorToInt()
    )