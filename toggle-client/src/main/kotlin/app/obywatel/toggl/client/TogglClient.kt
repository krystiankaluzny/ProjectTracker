package app.obywatel.toggl.client

import ch.simas.jtoggl.domain.Project
import ch.simas.jtoggl.domain.User
import ch.simas.jtoggl.domain.Workspace

interface TogglClient {

    val currentUser: User?
    val workspaces: List<Workspace>
    fun getWorkspaceProjects(id: Long): List<Project>
}
