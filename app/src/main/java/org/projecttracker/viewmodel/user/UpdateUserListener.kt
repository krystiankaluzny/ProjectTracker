package org.projecttracker.viewmodel.user

import org.projecttracker.model.entity.User

interface UpdateUserListener {
    fun onAddUser(position: Int, user: User)

    fun onUpdateUser(position: Int, user: User)
}