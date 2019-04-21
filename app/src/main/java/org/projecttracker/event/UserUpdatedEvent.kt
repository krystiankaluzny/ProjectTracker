package org.projecttracker.event

import org.projecttracker.model.entity.User

data class UserUpdatedEvent(
    val position: Int,
    val user: User
)