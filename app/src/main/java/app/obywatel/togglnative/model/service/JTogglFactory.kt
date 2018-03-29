package app.obywatel.togglnative.model.service

import ch.simas.jtoggl.JToggl

class JTogglFactory {

    fun jToggl(apiToken: String) : JToggl {
        val jToggl = JToggl(apiToken)
        jToggl.throttlePeriod = 1000L
        jToggl.switchLoggingOn()

        return jToggl
    }
}