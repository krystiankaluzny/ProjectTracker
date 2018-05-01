package app.obywatel.toggl.client.internal.retrofit.dto

data class TimeEntry(
    val id: Long?,
    val description: String?,
    val wid: Long?,
    val pid: Long?,
    val tid: Long?,
    val billable: Boolean?,
    val start: String,
    val stop: String?,
    val duration: Long?,
    val created_with: String,
    val tags: List<String>?,
    val at: String?
)