package app.obywatel.toggl.client.internal

import app.obywatel.toggl.client.TogglClient
import app.obywatel.toggl.client.entity.*
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

internal class TogglClientImpl(val apiToken: String) : TogglClient {

    private val togglApi = RetrofitFactory.create<TogglApi>(apiToken)

    override fun getCurrentUser(): User? {

        val userData = togglApi.me().execute().body()?.data ?: return null

        return userData.let {
            User(
                id = it.id,
                apiToken = it.api_token,
                defaultWorkspaceId = it.default_wid,
                fullName = it.fullname,
                email = it.email,
                beginningOfWeek = Day.fromValue(it.beginning_of_week),
                language = it.language,
                timezone = it.timezone,
                imageUrl = it.image_url ?: "",
                creationTimestamp = fromIsoTimestamp(it.created_at),
                lastUpdateTimestamp = fromIsoTimestamp(it.at)
            )
        }
    }

    override fun getWorkspaces(): List<Workspace> {

        val workspaces = togglApi.workspaces().execute().body() ?: return emptyList()

        return workspaces.map {
            Workspace(
                id = it.id,
                name = it.name,
                admin = it.admin,
                defaultCurrency = it.default_currency,
                logoUrl = it.logo_url ?: "",
                premium = it.premium,
                rounding = RoundingType.fromValue(it.rounding),
                roundingMinutes = it.rounding_minutes,
                onlyAdminsMayCreateProjects = it.only_admins_may_create_projects,
                onlyAdminsSeeBillableRates = it.only_admins_see_billable_rates,
                defaultHourlyRate = it.default_hourly_rate ?: 0.0,
                lastUpdateTimestamp = fromIsoTimestamp(it.at)
            )
        }
    }

    override fun getWorkspaceProjects(id: Long): List<Project> = emptyList()

    override fun getProjectTimeEntries(workspaceId: Long, projectId: Long): List<TimeEntry> {
        return emptyList()
    }

    private fun fromIsoTimestamp(isoString: String) = ZonedDateTime.parse(isoString, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toEpochSecond()
}

