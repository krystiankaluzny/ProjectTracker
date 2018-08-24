package app.obywatel.toggl.client.internal.retrofit.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

internal data class TimeEntryResponse(
    @JsonProperty("data") val timeEntry: TimeEntry
)

internal data class TimeEntryRequest(
    @JsonProperty("time_entry") val timeEntry: TimeEntry
)

@JsonInclude(JsonInclude.Include.NON_NULL)
internal data class TimeEntry(
    val id: Long?,
    val description: String?,
    val wid: Long?,
    val pid: Long?,
    val tid: Long?,
    val billable: Boolean?,
    val start: String,
    val stop: String?,
    val duration: Long?,
    val created_with: String?,
    val tags: List<String>?,
    val at: String?
)