package org.projecttracker.event

import org.projecttracker.model.entity.User

data class UserSelectedEvent(
    val user: User
)