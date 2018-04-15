package app.obywatel.toggl.client.internal

import app.obywatel.toggl.client.TogglClient
import app.obywatel.toggl.client.entity.Project
import app.obywatel.toggl.client.entity.TimeEntry
import app.obywatel.toggl.client.entity.User
import app.obywatel.toggl.client.entity.Workspace

internal class TogglClientImpl(val apiToken: String) : TogglClient {

    private val togglApi = RetrofitFactory.create<TogglApi>(apiToken)

    override fun getCurrentUser(): User? {

        val userData = togglApi.me().execute().body()?.data ?: return null

        return userData.toExternal()
    }

    override fun getWorkspaces(): List<Workspace> {

        val workspaces = togglApi.workspaces().execute().body() ?: return emptyList()

        return workspaces.map { it.toExternal() }
    }

    override fun getWorkspaceProjects(id: Long): List<Project> {

        val workspaceProjects = togglApi.workspaceProjects(id).execute().body() ?: return emptyList()

        return workspaceProjects.map { it.toExternal() }
    }

    override fun getProjectTimeEntries(workspaceId: Long, projectId: Long): List<TimeEntry> {
        return emptyList()
    }
}

