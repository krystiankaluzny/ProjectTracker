package app.obywatel.toggl.client.internal.retrofit.dto

import com.fasterxml.jackson.annotation.JsonUnwrapped

internal data class DetailedReportResponse(
    val total_count: Int,
    val per_page: Int,
    @field:JsonUnwrapped val reportApiResponse: ReportApiResponse<Unit>
)