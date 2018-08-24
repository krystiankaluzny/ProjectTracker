package app.obywatel.toggl.client.request

data class WeeklyReportParameters(
    val baseReportParameters: BaseReportParameters = BaseReportParameters(),
    val weeklyOrder: WeeklyOrder? = null
)

enum class WeeklyOrder(val field: String, val ascending: Boolean) {

    TITLE_ASC("title", true),
    TITLE_DESC("title", false),

    WEEK_TOTAL_ASC("week_total", true),
    WEEK_TOTAL_DESC("week_total", false),

    DAY1_ASC("day1", true),
    DAY1_DESC("day1", false),

    DAY2_ASC("day2", true),
    DAY2_DESC("day2", false),

    DAY3_ASC("day3", true),
    DAY3_DESC("day3", false),

    DAY4_ASC("day4", true),
    DAY4_DESC("day4", false),

    DAY5_ASC("day5", true),
    DAY5_DESC("day5", false),

    DAY6_ASC("day6", true),
    DAY6_DESC("day6", false),

    DAY7_ASC("day7", true),
    DAY7_DESC("day7", false)
}