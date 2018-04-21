package app.obywatel.toggl.client.internal.retrofit.dto

internal open class ApiResponse<out T>(
    val since: Long,
    val data: T
)

internal open class ReportApiResponse<out T>(
    val total_grand: Long?,
    val total_billable: Long?,
    val total_currencies: List<CurrencyAmount>,
    val data: List<T>
)

internal data class CurrencyAmount(
    val currency: String?,
    val amount: Double?
)