package org.projecttracker.viewmodel.user

interface UpdateUserListener {
    fun onAddUser(position: Int)

    fun onUpdateUser(position: Int)
}