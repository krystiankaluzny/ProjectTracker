package app.obywatel.togglnative.model.service

import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.entity.Workspace

fun ch.simas.jtoggl.domain.User.toEntity() = User(id, apiToken)
fun ch.simas.jtoggl.domain.Workspace.toEntity(userId: Long) = Workspace(id, name, userId)