package app.obywatel.togglnative.model.service.timer

import android.util.Log
import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.entity.Workspace
import app.obywatel.togglnative.model.entity.Workspace_Table
import app.obywatel.togglnative.model.service.toEntity
import ch.simas.jtoggl.JToggl
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.sql.language.SQLite.select

class TimerService(private val user: User, private val jToggl: JToggl) {
    companion object {
        private val TAG = "TimerService"
    }

    // @formatter:off
    fun getStoredWorkspaces(): MutableList<Workspace> = select()
                                                        .from(Workspace::class.java)
                                                        .where(Workspace_Table.user_id.eq(user.id))
                                                        .list
    // @formatter:on

    fun fetchWorkspaces() {

        jToggl.workspaces.forEach {
            Log.d(TAG, it.toString())
            it.toEntity(user).save()
        }
    }
}