package app.obywatel.togglnative.model.service

import app.obywatel.togglnative.model.entity.Project
import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.entity.Workspace

fun app.obywatel.toggl.client.entity.User.toEntity() = User(id, apiToken, fullName)
fun app.obywatel.toggl.client.entity.Workspace.toEntity(user: User) = Workspace(id, name, user)
fun app.obywatel.toggl.client.entity.Project.toEntity(workspace: Workspace) = Project(id, name, 0, workspace)