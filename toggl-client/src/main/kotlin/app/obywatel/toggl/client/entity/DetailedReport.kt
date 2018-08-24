package app.obywatel.toggl.client.entity

data class DetailedReport(
    val totalCount: Int,
    val perPage: Int,
    val totalGrand: Long?,
    val totalPayment: Long?,
    val totalCurrencies: List<CurrencyAmount>,
    val detailedTimeEntries: List<DetailedTimeEntry>
)

data class CurrencyAmount(
    val currency: String?,
    val amount: Double?
)

data class DetailedTimeEntry(
    val id: Long,
    val client: String?,
    val project: Info?,
    val task: Info?,
    val user: Info?,
    val description: String?,
    val startTimestamp: Long,
    val endTimestamp: Long?,
    val durationMillis: Long?,
    val lastUpdateTimestamp: Long,
    val useStop: Boolean,
    val billable: Boolean,
    val payment: Double?,
    val currency: String?,
    val tags: List<String>
) {
    data class Info(
        val id: Long,
        val name: String
    )
}