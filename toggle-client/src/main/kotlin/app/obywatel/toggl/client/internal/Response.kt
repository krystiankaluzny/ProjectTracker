package app.obywatel.toggl.client.internal

internal data class Response<out T>(
    val data: T
)