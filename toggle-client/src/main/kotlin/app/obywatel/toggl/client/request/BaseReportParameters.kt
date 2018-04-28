package app.obywatel.toggl.client.request

data class BaseReportParameters(
    val userAgent: String? = null,
    val fromTimestamp: Long? = null,
    val toTimestamp: Long? = null,
    val billable: Billable? = null,
    val clientIds: List<Long> = emptyList(),
    val projectIds: List<Long> = emptyList(),
    val userIds: List<Long> = emptyList(),
    val membersOfGroupIds: List<Long> = emptyList(),
    val orMembersOfGroupIds: List<Long> = emptyList(),
    val tagIds: List<Long> = emptyList(),
    val taskIds: List<Long> = emptyList(),
    val timeEntryIds: List<Long> = emptyList(),
    val description: String? = null,
    val withoutDescription: Boolean? = null,
    val distinctRates: Boolean? = null,
    val rounding: Boolean? = null
)


enum class Billable(val value: String) {
    YES("yes"),
    NO("no"),
    BOTH("both");
}

