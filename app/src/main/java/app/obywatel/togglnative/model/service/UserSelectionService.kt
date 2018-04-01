package app.obywatel.togglnative.model.service

import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.entity.User_Table
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.sql.language.SQLite.select
import com.raizlabs.android.dbflow.sql.language.SQLite.update

class UserSelectionService {

    fun getAllUsers(): MutableList<User> = SQLite.select().from(User::class).list

    fun selectUser(user: User) {
        user.selected = true
        user.update()

        update(User::class.java)
            .set(User_Table.selected eq false)
            .where(User_Table.id notEq user.id)
            .execute()

    }

    fun unselectUsers() {

        update(User::class.java)
            .set(User_Table.selected eq false)
            .execute()
    }

    // @formatter:off
    fun getSelectedUser(): User? = select()
                                       .from(User::class)
                                       .where(User_Table.selected eq true)
                                       .result

    // @formatter:on
}
