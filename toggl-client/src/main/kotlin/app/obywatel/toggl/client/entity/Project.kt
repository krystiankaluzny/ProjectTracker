package app.obywatel.toggl.client.entity

data class Project(
    val id: Long,
    val name: String,
    val workspaceId: Long,
    val clientId: Long?,
    val active: Boolean,
    val private: Boolean,
    val creationTimestamp: Long,
    val colorId: Int,
    val color: Int
)