package app.obywatel.toggl.client.internal

import app.obywatel.toggl.client.TogglClient
import app.obywatel.toggl.client.entity.*
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

internal class TogglClientImpl(val apiToken: String) : TogglClient {

    private val togglApi = RetrofitFactory.create<TogglApi>(apiToken)

    override fun getCurrentUser(): User? {

        val userData = togglApi.me().execute().body()?.data ?: return null

        return User(
            id = userData.id,
            apiToken = userData.api_token,
            defaultWorkspaceId = userData.default_wid,
            fullName = userData.fullname,
            email = userData.email,
            beginningOfWeek = Day.fromInt(userData.beginning_of_week),
            language = userData.language,
            timezone = userData.timezone,
            imageUrl = userData.image_url ?: "",
            creationDate = fromIsoString(userData.created_at),
            lastUpdateDate = fromIsoString(userData.at)
        )
    }

    override fun getWorkspaces(): List<Workspace> = emptyList()

    override fun getWorkspaceProjects(id: Long): List<Project> = emptyList()

    override fun getProjectTimeEntries(workspaceId: Long, projectId: Long): List<TimeEntry> {
        return emptyList()
    }

    private fun fromIsoString(isoString: String) = ZonedDateTime.parse(isoString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}

