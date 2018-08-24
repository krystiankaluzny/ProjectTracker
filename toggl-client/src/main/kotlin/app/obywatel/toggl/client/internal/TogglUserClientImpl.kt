package app.obywatel.toggl.client.internal

import app.obywatel.toggl.client.TogglUserClient
import app.obywatel.toggl.client.entity.User
import app.obywatel.toggl.client.internal.retrofit.TogglApi

internal class TogglUserClientImpl(private val togglApi: TogglApi) : TogglUserClient {

    override fun getCurrentUser(): User? = togglApi.me().execute().body()?.user?.toExternal()
}