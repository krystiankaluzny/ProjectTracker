package app.obywatel.toggl.client

import app.obywatel.toggl.client.entity.*
import app.obywatel.toggl.client.request.DetailedReportParameters
import app.obywatel.toggl.client.request.SummaryReportParameters
import app.obywatel.toggl.client.request.WeeklyReportParameters


interface TogglClient : TogglUserClient, TogglWorkspaceClient, TogglTimeEntityClient, TogglReportClient

interface TogglUserClient {

    fun getCurrentUser(): User?
}

interface TogglWorkspaceClient {

    fun getWorkspaces(): List<Workspace>
    fun getWorkspaceProjects(id: Long): List<Project>
}

interface TogglTimeEntityClient {

    fun getTimeEntriy(timeEntryId: Long): TimeEntry
    fun getRunningTimeEntriy(): TimeEntry?
    fun createTimeEntriy(timeEntry: TimeEntry): TimeEntry
    fun startTimeEntriy(timeEntry: TimeEntry): TimeEntry
    fun stopTimeEntriy(timeEntry: TimeEntry): TimeEntry
    fun updateTimeEntriy(timeEntry: TimeEntry): TimeEntry
    fun deleteTimeEntriy(timeEntryId: Long): Boolean
    fun updateTimeEntriesTags(timeEntryIds: List<Long>, tags: List<String>, updateTagsAction: UpdateTagsAction)

    enum class UpdateTagsAction {
        OVERRIDE, ADD, REMOVE
    }
}

interface TogglReportClient {

    fun getWeeklyReport(workspaceId: Long, weeklyReportParameters: WeeklyReportParameters = WeeklyReportParameters())
    fun getDetailedReport(workspaceId: Long, detailedReportParameters: DetailedReportParameters = DetailedReportParameters()): DetailedReport
    fun getSummaryReport(workspaceId: Long, summaryReportParameters: SummaryReportParameters = SummaryReportParameters())
}