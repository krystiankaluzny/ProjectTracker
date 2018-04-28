package app.obywatel.togglnative.viewmodel.user

import app.obywatel.togglnative.model.entity.User

interface SelectUserListener {
    fun onSelectUser(user: User)
}