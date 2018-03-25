package app.obywatel.togglnative.viewmodel.user

import app.obywatel.togglnative.model.repository.UserItem

class SingleUserViewModel internal constructor(userItem: UserItem) {

    val userId: String = userItem.id.toString()
    val userName: String = userItem.name
}