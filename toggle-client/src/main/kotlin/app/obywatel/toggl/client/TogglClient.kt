package app.obywatel.toggl.client

import app.obywatel.toggl.client.entity.*
import app.obywatel.toggl.client.request.DetailedReportParameters
import app.obywatel.toggl.client.request.SummaryReportParameters
import app.obywatel.toggl.client.request.WeeklyReportParameters


interface TogglClient {

    fun getCurrentUser(): User?
    fun getWorkspaces(): List<Workspace>
    fun getWorkspaceProjects(id: Long): List<Project>

    fun getWeeklyReport(workspaceId: Long, weeklyReportParameters: WeeklyReportParameters = WeeklyReportParameters())
    fun getDetailedReport(workspaceId: Long, detailedReportParameters: DetailedReportParameters = DetailedReportParameters()) : DetailedReport
    fun getSummaryReport(workspaceId: Long, summaryReportParameters: SummaryReportParameters = SummaryReportParameters())
}
