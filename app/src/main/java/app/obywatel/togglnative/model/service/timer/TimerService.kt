package app.obywatel.togglnative.model.service.timer

import android.util.Log
import ch.simas.jtoggl.JToggl
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class TimerService(val jToggl: JToggl) {
    companion object {
        private val TAG = "TimerService"
    }

    fun getWorkspaces() {
        launch(UI) {
            async(CommonPool) {

                jToggl.workspaces.forEach {
                    Log.d(TAG, it.toString())
                }
            }
        }
    }
}