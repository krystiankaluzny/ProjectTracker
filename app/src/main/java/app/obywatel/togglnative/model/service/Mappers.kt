package app.obywatel.togglnative.model.service

import app.obywatel.togglnative.model.entity.Project
import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.entity.Workspace

fun ch.simas.jtoggl.domain.User.toEntity() = User(id, apiToken, fullname)
fun ch.simas.jtoggl.domain.Workspace.toEntity(user: User) = Workspace(id, name, user)
fun ch.simas.jtoggl.domain.Project.toEntity(workspace: Workspace) = Project(id, name, 0, workspace)