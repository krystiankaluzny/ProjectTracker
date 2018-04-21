package app.obywatel.toggl.client.request

data class DetailedReportParameters(
    val baseReportParameters: BaseReportParameters = BaseReportParameters(),
    val detailedOrder: DetailedOrder? = null
)

enum class DetailedOrder(field: String, ascending: Boolean) {

    DATE_ASC("date", true),
    DATE_DESC("date", false),

    DESCRIPTION_ASC("description", true),
    DESCRIPTION_DESC("description", false),

    DURATION_ASC("duration", true),
    DURATION_DESC("duration", false),

    USER_ASC("user", true),
    USER_DESC("user", false),
}