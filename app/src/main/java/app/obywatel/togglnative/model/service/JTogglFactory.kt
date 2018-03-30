package app.obywatel.togglnative.model.service

import ch.simas.jtoggl.JToggl
import javax.ws.rs.client.Client

class JTogglFactory {

    fun jToggl(apiToken: String) : JToggl {
        val jToggl = AndroidJToggle(apiToken)
        jToggl.throttlePeriod = 1000L
        jToggl.switchLoggingOn()

        return jToggl
    }

    internal class AndroidJToggle(apiToken: String) : JToggl(apiToken) {

        override fun prepareApiClient(): Client {
            val apiClient: Client = super.prepareApiClient()
            apiClient.register(JToggl.AndroidFriendlyFeature())

            return apiClient
        }
    }
}