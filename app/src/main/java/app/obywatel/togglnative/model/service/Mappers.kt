package app.obywatel.togglnative.model.service

import app.obywatel.togglnative.model.entity.User

fun ch.simas.jtoggl.domain.User.toEntity() = User(id, apiToken)