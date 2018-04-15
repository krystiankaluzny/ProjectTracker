package app.obywatel.toggl.client.internal.entity

internal data class Project(
    val id: Long,
    val name: String,
    val wid: Long,
    val cid: Long?,
    val active: Boolean,
    val is_private: Boolean,
    val template: Boolean?,
    val template_id: Long,
    val billable: Boolean?,
    val auto_estimates: Boolean?,
    val estimated_hours: Int?,
    val at: String,
    val color: Int,
    val hex_color: String,
    val rate: Double?
)