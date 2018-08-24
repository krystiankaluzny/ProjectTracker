package app.obywatel.toggl.client.internal.retrofit.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal data class UserResponse(
    val since: Long,
    @JsonProperty("data") val user: User
)

internal data class User(
    val id: Long,
    val api_token: String,
    val default_wid: Long,
    val email: String,
    val fullname: String,
    val jquery_timeofday_format: String,
    val jquery_date_format: String,
    val timeofday_format: String,
    val date_format: String,
    val store_start_and_stop_time: Boolean,
    val beginning_of_week: Byte,
    val language: String,
    val image_url: String?,
    val sidebar_piechart: Boolean,
    val at: String,
    val created_at: String,
    val send_product_emails: Boolean,
    val send_weekly_report: Boolean,
    val send_timer_notifications: Boolean,
    val openid_enabled: Boolean,
    val timezone: String
)