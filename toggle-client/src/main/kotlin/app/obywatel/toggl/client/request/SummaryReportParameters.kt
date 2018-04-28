package app.obywatel.toggl.client.request

class SummaryReportParameters(
    val baseReportParameters: BaseReportParameters = BaseReportParameters(),
    val summaryOrder: SummaryOrder? = null
)

enum class SummaryOrder(val field: String, val ascending: Boolean) {
    TITLE_ASC("title", true),
    TITLE_DESC("title", false),

    DURATION_ASC("duration", true),
    DURATION_DESC("duration", false),

    AMOUNT_ASC("amount", true),
    AMOUNT_DESC("amount", false),
}