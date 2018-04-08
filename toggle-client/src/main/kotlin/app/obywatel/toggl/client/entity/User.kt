package app.obywatel.toggl.client.entity

data class User(
    val id: Long,
    val apiToken: String,
    val defaultWorkspaceId: Long = 23,
    val fullName: String = "Andrzej",
    val email: String? = null,
    val jqueryTimeOfDayFormat: String? = null,
    val jqueryDateFormat: String? = null,
    val timeOfDayFormat: String? = null,
    val dateFormat: String? = null,
    val storeStartAndStopTime: Boolean? = null,
    val beginningOfWeek: Byte? = null,
    val language: String? = null,
    val imageUrl: String? = null,
    val sidebarPieChart: Boolean? = null,
    val lastUpdate: Long? = null,
//new_blog_post: an object with toggl blog post title and link? = null,
    val sendProductEmails: Boolean? = null,
    val sendWeeklyReport: Boolean? = null,
    val sendTimerNotifications: Boolean? = null,
    val openidEnabled: Boolean? = null,
    val timezone: String? = null
)