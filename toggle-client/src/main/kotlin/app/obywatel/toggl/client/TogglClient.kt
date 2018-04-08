package app.obywatel.toggl.client

import app.obywatel.toggl.client.entity.Project
import app.obywatel.toggl.client.entity.TimeEntry
import app.obywatel.toggl.client.entity.User
import app.obywatel.toggl.client.entity.Workspace


interface TogglClient {

    fun getCurrentUser(): User?
    fun getWorkspaces(): List<Workspace>
    fun getWorkspaceProjects(id: Long): List<Project>
    fun getProjectTimeEntries(workspaceId: Long, projectId: Long): List<TimeEntry>
}
