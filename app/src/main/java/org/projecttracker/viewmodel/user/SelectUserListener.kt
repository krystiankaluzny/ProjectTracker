package org.projecttracker.viewmodel.user

import org.projecttracker.model.entity.User

interface SelectUserListener {
    fun onSelectUser(user: User)
}