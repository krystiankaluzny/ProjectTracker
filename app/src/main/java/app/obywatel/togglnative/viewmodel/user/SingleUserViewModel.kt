package app.obywatel.togglnative.viewmodel.user

import app.obywatel.togglnative.model.repository.User

class SingleUserViewModel internal constructor(user: User) {

    val userId: String = user.id.toString()
    val userName: String = user.apiToken!!
}