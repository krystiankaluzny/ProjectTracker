package app.obywatel.toggl.client.internal

import app.obywatel.toggl.client.TogglReportClient
import app.obywatel.toggl.client.entity.DetailedReport
import app.obywatel.toggl.client.internal.retrofit.TogglReportApi
import app.obywatel.toggl.client.request.BaseReportParameters
import app.obywatel.toggl.client.request.DetailedReportParameters
import app.obywatel.toggl.client.request.SummaryReportParameters
import app.obywatel.toggl.client.request.WeeklyReportParameters
import app.obywatel.toggl.client.secondsToLocalDateStr

internal class TogglReportClientImpl(private val togglReportApi: TogglReportApi) : TogglReportClient {

    private companion object {
        const val userAgent = "TogglClient"
        val emptyDetailedReport = DetailedReport(0, 0, 0, 0, emptyList(), emptyList())
    }

    override fun getWeeklyReport(workspaceId: Long, weeklyReportParameters: WeeklyReportParameters) {
        val params = createBaseRequestParams(workspaceId, weeklyReportParameters.baseReportParameters)

        weeklyReportParameters.weeklyOrder?.let {
            params["order_field"] = it.field
            params["order_desc"] = if (it.ascending) "off" else "on"
        }
    }

    override fun getDetailedReport(workspaceId: Long, detailedReportParameters: DetailedReportParameters): DetailedReport {
        val params = createBaseRequestParams(workspaceId, detailedReportParameters.baseReportParameters)

        detailedReportParameters.detailedOrder?.let {
            params["order_field"] = it.field
            params["order_desc"] = if (it.ascending) "off" else "on"
        }
//        detailedReportParameters.page

        val detailedReport = togglReportApi.detailed(params).execute().body() ?: return emptyDetailedReport

        return detailedReport.toExternal()
    }

    override fun getSummaryReport(workspaceId: Long, summaryReportParameters: SummaryReportParameters) {
        val params = createBaseRequestParams(workspaceId, summaryReportParameters.baseReportParameters)

        summaryReportParameters.summaryOrder?.let {
            params["order_field"] = it.field
            params["order_desc"] = if (it.ascending) "off" else "on"
        }
    }

    private fun createBaseRequestParams(workspaceId: Long, baseReportParameters: BaseReportParameters): MutableMap<String, String> {

        val separator = ","
        val params = mutableMapOf<String, String>()

        params["workspace_id"] = workspaceId.toString()

        baseReportParameters.userAgent.let { params["user_agent"] = it ?: userAgent }
        baseReportParameters.fromTimestamp?.let { params["since"] = it.secondsToLocalDateStr() }
        baseReportParameters.toTimestamp?.let { params["until"] = it.secondsToLocalDateStr() }
        baseReportParameters.billable?.let { params["billable"] = it.value }
        baseReportParameters.clientIds.let { params["client_ids"] = it.joinToString(separator = separator) }
        baseReportParameters.membersOfGroupIds.let { params["members_of_group_ids"] = it.joinToString(separator = separator) }
        baseReportParameters.orMembersOfGroupIds.let { params["or_members_of_group_ids"] = it.joinToString(separator = separator) }
        baseReportParameters.tagIds.let { params["tag_ids"] = it.joinToString(separator = separator) }
        baseReportParameters.taskIds.let { params["task_ids"] = it.joinToString(separator = separator) }
        baseReportParameters.timeEntryIds.let { params["time_entry_ids"] = it.joinToString(separator = separator) }
        baseReportParameters.description?.let { params["description"] = it }
        baseReportParameters.withoutDescription?.let { params["without_description"] = if (it) "true" else "false" }
        baseReportParameters.distinctRates?.let { params["distinct_rates"] = if (it) "on" else "off" }
        baseReportParameters.rounding?.let { params["rounding"] = if (it) "on" else "off" }

        return params
    }
}