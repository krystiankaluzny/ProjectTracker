package app.obywatel.toggl.client.internal

import app.obywatel.toggl.client.TogglClient
import app.obywatel.toggl.client.TogglTimeEntityClient
import app.obywatel.toggl.client.entity.TimeEntry
import app.obywatel.toggl.client.internal.retrofit.TogglApi
import app.obywatel.toggl.client.internal.retrofit.TogglReportApi
import app.obywatel.toggl.client.request.DetailedReportParameters
import app.obywatel.toggl.client.request.SummaryReportParameters
import app.obywatel.toggl.client.request.WeeklyReportParameters

internal class TogglClientImpl(togglApi: TogglApi, togglReportApi: TogglReportApi) : TogglClient {

    private val togglUserClient = TogglUserClientImpl(togglApi)
    private val togglWorkspaceClient = TogglWorkspaceClientImpl(togglApi)
    private val togglTimeEntityClient = TogglTimeEntityClientImpl(togglApi)
    private val togglReportClient = TogglReportClientImpl(togglReportApi)

    override fun getCurrentUser() = togglUserClient.getCurrentUser()

    override fun getWorkspaces() = togglWorkspaceClient.getWorkspaces()
    override fun getWorkspaceProjects(id: Long) = togglWorkspaceClient.getWorkspaceProjects(id)

    override fun getTimeEntriy(timeEntryId: Long) = togglTimeEntityClient.getTimeEntriy(timeEntryId)
    override fun getRunningTimeEntriy() = togglTimeEntityClient.getRunningTimeEntriy()
    override fun createTimeEntry(timeEntry: TimeEntry) = togglTimeEntityClient.createTimeEntry(timeEntry)
    override fun startTimeEntriy(timeEntry: TimeEntry) = togglTimeEntityClient.startTimeEntriy(timeEntry)
    override fun stopTimeEntriy(timeEntry: TimeEntry) = togglTimeEntityClient.stopTimeEntriy(timeEntry)
    override fun updateTimeEntriy(timeEntry: TimeEntry) = togglTimeEntityClient.updateTimeEntriy(timeEntry)
    override fun deleteTimeEntriy(timeEntryId: Long) = togglTimeEntityClient.deleteTimeEntriy(timeEntryId)
    override fun updateTimeEntriesTags(timeEntryIds: List<Long>, tags: List<String>, updateTagsAction: TogglTimeEntityClient.UpdateTagsAction) = togglTimeEntityClient.updateTimeEntriesTags(timeEntryIds, tags, updateTagsAction)

    override fun getWeeklyReport(workspaceId: Long, weeklyReportParameters: WeeklyReportParameters) = togglReportClient.getWeeklyReport(workspaceId, weeklyReportParameters)
    override fun getDetailedReport(workspaceId: Long, detailedReportParameters: DetailedReportParameters) = togglReportClient.getDetailedReport(workspaceId, detailedReportParameters)
    override fun getSummaryReport(workspaceId: Long, summaryReportParameters: SummaryReportParameters) = togglReportClient.getSummaryReport(workspaceId, summaryReportParameters)
}
