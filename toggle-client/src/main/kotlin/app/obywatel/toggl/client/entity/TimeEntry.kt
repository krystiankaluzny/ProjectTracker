package app.obywatel.toggl.client.entity

data class TimeEntry(
    val id: Long?,
    val description: String?,
    val workspaceId: Long?,
    val projectId: Long?,
    val taskId: Long?,
    val billable: Boolean?,
    val startTimestamp: Long,
    val endTimestamp: Long?,
    val durationSeconds: Long?,
    val createdWith: String,
    val tags: List<String>?,
    val lastUpdateTimestamp: Long?
)