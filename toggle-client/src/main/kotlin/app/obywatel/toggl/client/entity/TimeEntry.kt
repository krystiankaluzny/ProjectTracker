package app.obywatel.toggl.client.entity

data class TimeEntry(
    val id: Long? = null,
    val description: String? = null,
    val workspaceId: Long? = null,
    val projectId: Long? = null,
    val taskId: Long? = null,
    val billable: Boolean? = null,
    val endTimestamp: Long? = null,
    val startTimestamp: Long,
    val durationSeconds: Long? = null,
    val createdWith: String? = null,
    val tags: List<String>? = emptyList(),
    val lastUpdateTimestamp: Long? = null
)