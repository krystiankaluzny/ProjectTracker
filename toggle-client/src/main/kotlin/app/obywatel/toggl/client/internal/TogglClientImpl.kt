package app.obywatel.toggl.client.internal

import app.obywatel.toggl.client.TogglClient
import app.obywatel.toggl.client.entity.Project
import app.obywatel.toggl.client.entity.TimeEntry
import app.obywatel.toggl.client.entity.User
import app.obywatel.toggl.client.entity.Workspace

internal class TogglClientImpl(val apiToken: String) : TogglClient {

    private val togglApi = RetrofitFactory.create<TogglApi>(apiToken)

    override fun getCurrentUser(): User? {

        val body = togglApi.me().execute().body()
        println(body)

        return User(34, "dupa")
    }

    override fun getWorkspaces(): List<Workspace> = emptyList()

    override fun getWorkspaceProjects(id: Long): List<Project> = emptyList()

    override fun getProjectTimeEntries(workspaceId: Long, projectId: Long): List<TimeEntry> {
        return emptyList()
    }
}