package org.projecttracker.event

import org.projecttracker.model.entity.User

data class UserAddedEvent(
    val position: Int,
    val user: User
)