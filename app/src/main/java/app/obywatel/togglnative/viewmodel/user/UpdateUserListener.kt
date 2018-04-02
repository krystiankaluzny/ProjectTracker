package app.obywatel.togglnative.viewmodel.user

interface UpdateUserListener {
    fun onAddUser(position: Int)

    fun onUpdateUser(position: Int)
}