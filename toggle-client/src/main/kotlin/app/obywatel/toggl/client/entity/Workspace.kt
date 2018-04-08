package app.obywatel.toggl.client.entity

data class Workspace(
    val id: Long,
    val name: String,
    val premium: Boolean,
    val admin: Boolean,
    val default_hourly_rate: Double,
    val default_currency: String,
    val only_admins_may_create_projects: Boolean,
    val only_admins_see_billable_rates: Boolean,
    val rounding: Int,
    val rounding_minutes: Int,
    val at: Long,
    val logo_url: String
)