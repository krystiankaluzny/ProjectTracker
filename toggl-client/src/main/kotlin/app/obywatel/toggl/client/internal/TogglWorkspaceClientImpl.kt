package app.obywatel.toggl.client.internal

import app.obywatel.toggl.client.TogglWorkspaceClient
import app.obywatel.toggl.client.entity.Project
import app.obywatel.toggl.client.entity.Workspace
import app.obywatel.toggl.client.internal.retrofit.TogglApi

internal class TogglWorkspaceClientImpl(private val togglApi: TogglApi) : TogglWorkspaceClient {

    override fun getWorkspaces(): List<Workspace> {

        val workspaces = togglApi.workspaces().execute().body() ?: return emptyList()

        return workspaces.map { it.toExternal() }
    }

    override fun getWorkspaceProjects(id: Long): List<Project> {

        val workspaceProjects = togglApi.workspaceProjects(id).execute().body() ?: return emptyList()

        return workspaceProjects.map { it.toExternal() }
    }
}