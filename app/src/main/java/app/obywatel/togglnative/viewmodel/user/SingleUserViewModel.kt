package app.obywatel.togglnative.viewmodel.user

import app.obywatel.togglnative.model.entity.User

class SingleUserViewModel internal constructor(private val user: User, private val userViewModel: UserViewModel) {

    val userId: String = user.id.toString()
    val userName: String = user.apiToken

    fun selectUser() = userViewModel.selectUser(user)
}