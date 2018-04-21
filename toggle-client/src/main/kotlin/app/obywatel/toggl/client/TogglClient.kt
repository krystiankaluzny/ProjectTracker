package app.obywatel.toggl.client

import app.obywatel.toggl.client.entity.Project
import app.obywatel.toggl.client.entity.TimeEntry
import app.obywatel.toggl.client.entity.User
import app.obywatel.toggl.client.entity.Workspace
import app.obywatel.toggl.client.request.DetailedReportParameters
import app.obywatel.toggl.client.request.SummaryReportParameters
import app.obywatel.toggl.client.request.WeeklyReportParameters


interface TogglClient {

    fun getCurrentUser(): User?
    fun getWorkspaces(): List<Workspace>
    fun getWorkspaceProjects(id: Long): List<Project>
    fun getProjectTimeEntries(workspaceId: Long, projectId: Long): List<TimeEntry>

    fun getWeeklyReport(workspaceId: Long, weeklyReportParameters: WeeklyReportParameters = WeeklyReportParameters())
    fun getDetailedReport(workspaceId: Long, detailedReportParameters: DetailedReportParameters = DetailedReportParameters())
    fun getSummaryReport(workspaceId: Long, summaryReportParameters: SummaryReportParameters = SummaryReportParameters())
}
