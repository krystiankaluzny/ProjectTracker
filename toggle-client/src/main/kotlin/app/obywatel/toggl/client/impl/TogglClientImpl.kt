package app.obywatel.toggl.client.impl

import app.obywatel.toggl.client.TogglClient
import ch.simas.jtoggl.JToggl
import ch.simas.jtoggl.domain.Project
import ch.simas.jtoggl.domain.User
import ch.simas.jtoggl.domain.Workspace

internal class TogglClientImpl(val jToggl: JToggl) : TogglClient {

    override val currentUser: User?
        get() = jToggl.currentUser

    override val workspaces: List<Workspace>
        get() = jToggl.workspaces

    override fun getWorkspaceProjects(id: Long): List<Project> = jToggl.getWorkspaceProjects(id)


}